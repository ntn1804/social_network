package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.ImageService;
import com.example.socialnetwork.util.FileUtils;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        if (multipartFile.getContentType() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "please choose a file");
        }

        // Check file's mimeType
        String mimeType = FileUtils.getRealMimeType(multipartFile);
        if (!mimeType.equals("image/jpeg")) {
            throw new InvalidMediaTypeException(multipartFile.getContentType(), "please choose an image file");
        }

        // Save file to static folder
        Path folderPath = Paths.get("src/main/resources/static/images/avatar");
        Path filePath = FileUtils.imageUploadUtil(multipartFile, folderPath);

        imageRepository.save(Avatar.builder()
                .name(filePath.getFileName().toString())
                .type(multipartFile.getContentType())
                .filePath(filePath.toFile().getPath())
                .isDeleted(0)
                .defaultAvatar(0)
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
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        return getAvatar(user);
    }

    @Override
    public ResponseEntity<?> showOthersAvatar(Long userId) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        return getAvatar(user);
    }

    private ResponseEntity<?> getAvatar(User user) throws IOException {
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
    public Response deleteAvatar(Long avatarId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "User not found"));

        Optional<Avatar> optionalAvatar = imageRepository.findById(avatarId);
        Avatar avatar = optionalAvatar
                .orElseThrow(() -> new GeneralException(HttpStatus.BAD_REQUEST, "Avatar not found"));

        if (!avatar.getUser().getId().equals(user.getId())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Not your avatar");
        }

        if (avatar.getDefaultAvatar() == 1) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Can not delete default avatar");
        }

        imageRepository.delete(avatar);

        return Response.builder()
                .responseMessage("Deleted avatar successfully")
                .build();
    }
}
