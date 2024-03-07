package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.ImageService;
import com.example.socialnetwork.util.FileUploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public Response uploadAvatar(MultipartFile multipartFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "User not found"));

        if (multipartFile.getContentType() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "please choose a file");
        }

        MediaType mediaType = MediaType.parseMediaType(multipartFile.getContentType());
        if (!mediaType.getType().equals("image")) {
            throw new InvalidMediaTypeException(multipartFile.getContentType(), "please choose an image file");
        }

        // Save file to static folder
        Path folder = Paths.get("src/main/resources/static/images");
        String filename = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        Path file = Files.createTempFile(folder, filename + "-", "." + extension);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
        }

        imageRepository.save(Avatar.builder()
                .name(file.getFileName().toString())
                .type(multipartFile.getContentType())
                .filePath(file.toFile().getPath())
                .isDeleted(0)
                .user(user)
                .build());

        return Response.builder()
                .responseMessage("Avatar uploaded successfully")
                .build();
    }

    public ResponseEntity<?> showMyAvatar() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "User not found"));

        Avatar avatar = imageRepository.findByUserId(user.getId());

        if (avatar == null) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Avatar not found");
        }

        String filePath = avatar.getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    @Override
    public ResponseEntity<?> showOthersAvatar(Long userId) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "User not found"));

        Avatar avatar = imageRepository.findByUserId(user.getId());
        if (avatar == null) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Avatar not found");
        }

        String filePath = avatar.getFilePath() + ".jpg";
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }
}
