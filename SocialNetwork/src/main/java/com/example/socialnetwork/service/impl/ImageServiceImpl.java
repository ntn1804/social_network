package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private final String companyFolder = "/static/image/";
    private final String homeFolder = "C:\\Users\\MY PC\\Desktop\\Works\\MyFiles\\";

    public Response uploadAvatar(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "User not found"));

        if (file.getContentType() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "please choose a file");
        }

        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        if (!mediaType.getType().equals("image")) {
            throw new InvalidMediaTypeException(file.getContentType(), "please choose an image file");
        }

            UUID uuid = UUID.randomUUID();
            String stringUuid = uuid.toString();

            String filePath = companyFolder + stringUuid;

            imageRepository.save(Avatar.builder()
                    .name(stringUuid)
                    .type(file.getContentType())
                    .filePath(filePath)
                    .isDeleted(0)
                    .user(user)
                    .build());

//            file.transferTo(new File(filePath + ".jpg"));

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

        String filePath = avatar.getFilePath() + ".jpg";
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
