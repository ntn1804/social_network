package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PostRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Override
    public ResponseEntity<Response> createPost(MultipartFile file, PostRequestDTO requestDTO) throws IOException {
        // use jwt to get username.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        String filePath = null;
        if (file != null) {
            filePath = folderPath + file.getOriginalFilename();
        }

        if (file == null && requestDTO == null){
            return ResponseEntity.ok(Response.builder()
                    .statusCode(400)
                    .responseMessage("Post is empty")
                    .build());
        } else {
            Post post = Post.builder()
                    .text(requestDTO != null ? requestDTO.getText() : null)
                    .user(user.orElse(null))
                    .image(file != null ? file.getOriginalFilename() : null)
                    .filePath(file != null ? filePath : null)
                    .build();
            postRepo.save(post);

            if (file != null) {
                file.transferTo(new File(filePath));
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Created post successfully")
                .build());
    }

    @Override
    public ResponseEntity<Response> editPost(MultipartFile file, PostRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        return null;
    }
}
