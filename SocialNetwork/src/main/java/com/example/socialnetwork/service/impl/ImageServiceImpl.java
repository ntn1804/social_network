package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\";

    public Response uploadAvatar(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please login");
        }

        if (file.getContentType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose a file");
        }

        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        if (!mediaType.getType().equals("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose an image file");
        } else {
            UUID uuid = UUID.randomUUID();
            String stringUuid = uuid.toString();

            String filePath = folderPath + stringUuid;

            imageRepository.save(Avatar.builder()
                    .name(stringUuid)
                    .type(file.getContentType())
                    .filePath(filePath)
                    .isDeleted(0)
                    .user(optionalUser.get())
                    .build());

            file.transferTo(new File(filePath + ".jpg"));

            return Response.builder()
                    .responseMessage("file uploaded successfully")
                    .build();
        }
    }

    public ResponseEntity<?> showMyAvatar() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            Avatar avatar = imageRepository.findByUserId(user.getId());

                String filePath = avatar.getFilePath() + ".jpg";
                byte[] images = Files.readAllBytes(new File(filePath).toPath());
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.valueOf("image/png"))
                        .body(images);

        }
        return null;
    }
}
