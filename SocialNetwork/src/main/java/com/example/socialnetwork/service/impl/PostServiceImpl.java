package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.ShowAllPostResponseDTO;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.mapper.PostMapper;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    private final String folderPath = "C:\\Users\\MY PC\\Desktop\\Dev9\\MyFiles\\Post\\";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private PostMapper postMapper;

    @Override
    public ResponseEntity<Response> createPost(MultipartFile file, PostRequestDTO requestDTO) throws IOException {
        // use jwt to get username.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

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
            postRepository.save(post);

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
            Optional<Post> oldPost = postRepository.findById(postId);
            if (oldPost.isPresent()) {
                // set new attribute (image + text)
                Post newPost = oldPost.get();
                newPost.setText(requestDTO != null ? requestDTO.getText() : null);
                newPost.setImage(file != null ? file.getOriginalFilename() : null);
                newPost.setFilePath(file != null ? filePath : null);
                postRepository.save(newPost);
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Edited post successfully")
                .build());
    }

    @Override
    public ResponseEntity<List<ShowAllPostResponseDTO>> getAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            Long userId = user.get().getId();
            List<Friend> friendList = friendRepository.findAll();
            List<Long> usersFriends = new ArrayList<>();

            for (Friend usersFriend : friendList) {
                if (usersFriend.getRequestStatus().equals("Accepted")) {
                    if (usersFriend.getFriend().getId().equals(userId)) {
                        usersFriends.add(usersFriend.getUser().getId());
                    }
                    if (usersFriend.getUser().getId().equals(userId)) {
                        usersFriends.add(usersFriend.getFriend().getId());
                    }
                }
            }
            usersFriends.add(userId);

            List<Post> timelinePost = postRepository.findAllByUserIdIn(usersFriends);

//            timelinePost.sort(Collections.reverseOrder());
            Collections.sort(timelinePost);

            return ResponseEntity.ok(postMapper.convertPostToShowAllPostResponseDTO(timelinePost));
        }
        return null;
    }
}
