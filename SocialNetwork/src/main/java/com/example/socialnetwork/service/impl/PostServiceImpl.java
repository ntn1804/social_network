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
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

//  private final String folderPath = "C:\\Users\\MY PC\\Desktop\\Dev9\\MyFiles\\Post\\";
    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private PostMapper postMapper;

    @Override
    public Response createPost(MultipartFile file, PostRequestDTO requestDTO) throws IOException {
        // use jwt to get username.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
            }

        if (file == null && requestDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post is empty");
        }

        if (requestDTO != null && file == null) {
            Post post = Post.builder()
                    .text(requestDTO.getText())
                    .user(optionalUser.get())
                    .privacy("public")
                    .build();

            postRepository.save(post);

            return Response.builder()
                    .responseMessage("Created post successfully")
                    .build();
        }

        if (requestDTO != null && file != null) {
            if (file.getContentType() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose a file");
            }

            MediaType mediaType = MediaType.parseMediaType(file.getContentType());
            if (!mediaType.getType().equals("image")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose an image file");
            }

            UUID uuid = UUID.randomUUID();
            String stringUuid = uuid.toString();
            String filePath = folderPath + stringUuid;

            Post post = Post.builder()
                    .user(optionalUser.get())
                    .image(stringUuid)
                    .filePath(filePath)
                    .text(requestDTO.getText())
                    .privacy("public")
                    .build();

            postRepository.save(post);

            file.transferTo(new File(filePath + ".jpg"));

            return Response.builder()
                    .responseMessage("Created post successfully")
                    .build();
        }

        if (requestDTO == null && file != null) {
            if (file.getContentType() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose a file");
            }

            MediaType mediaType = MediaType.parseMediaType(file.getContentType());
            if (!mediaType.getType().equals("image")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose an image file");
            }

            UUID uuid = UUID.randomUUID();
            String stringUuid = uuid.toString();
            String filePath = folderPath + stringUuid;

            Post post = Post.builder()
                    .user(optionalUser.get())
                    .image(stringUuid)
                    .filePath(filePath)
                    .privacy("public")
                    .build();

            postRepository.save(post);

            file.transferTo(new File(filePath + ".jpg"));

            return Response.builder()
                    .responseMessage("Created post successfully")
                    .build();
        }
        return null;
    }

    @Override
    public ResponseEntity<Response> editPost(Long postId, MultipartFile file, PostRequestDTO requestDTO) {
        String filePath = null;
        if (file != null) {
            filePath = folderPath + file.getOriginalFilename();
        }

        if (file == null && requestDTO == null) {
            return ResponseEntity.ok(Response.builder()
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
            List<Long> usersFriendsId = new ArrayList<>();

            for (Friend userFriends : friendList) {
                if (userFriends.getRequestStatus().equals("Accepted")) {
                    if (userFriends.getFriend().getId().equals(userId)) {
                        usersFriendsId.add(userFriends.getUser().getId());
                    }
                    if (userFriends.getUser().getId().equals(userId)) {
                        usersFriendsId.add(userFriends.getFriend().getId());
                    }
                }
            }
            usersFriendsId.add(userId);

            List<Post> timelinePost = postRepository.findAllByUserIdIn(usersFriendsId);

            Collections.sort(timelinePost);

            return ResponseEntity.ok(postMapper.convertPostToShowAllPostResponseDTO(timelinePost));
        }
        return null;
    }
}
