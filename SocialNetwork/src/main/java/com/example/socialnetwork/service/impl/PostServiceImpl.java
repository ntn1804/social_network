package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private FriendRepo friendRepo;

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

        if (file == null && requestDTO == null) {
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
    public ResponseEntity<Response> editPost(Long postId, MultipartFile file, PostRequestDTO requestDTO) {
        String filePath = null;
        if (file != null) {
            filePath = folderPath + file.getOriginalFilename();
        }

        if (file == null && requestDTO == null) {
            return ResponseEntity.ok(Response.builder()
                    .statusCode(400)
                    .responseMessage("Post is empty")
                    .build());
        }

        // find old post by ID
        if (postId != null) {
            Optional<Post> oldPost = postRepo.findById(postId);
            if (oldPost.isPresent()) {
                // set new attribute (image + text)
                Post newPost = oldPost.get();
                newPost.setText(requestDTO != null ? requestDTO.getText() : null);
                newPost.setImage(file != null ? file.getOriginalFilename() : null);
                newPost.setFilePath(file != null ? filePath : null);
                postRepo.save(newPost);
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Edited post successfully")
                .build());
    }

    @Override
    public ResponseEntity<Response> getFriendsPost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        List<Friend> friendList = friendRepo.findByUserId(user.get().getId());
        List<Friend> usersFriends = new ArrayList<>();

        for (Friend usersFriend : friendList) {
            if (usersFriend.getIsFriend().equals("1")){
                usersFriends.add(usersFriend);
            }
        }
        return null;
    }
}
