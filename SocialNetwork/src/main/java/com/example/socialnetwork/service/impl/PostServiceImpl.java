package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostImageDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserDTO;
import com.example.socialnetwork.entity.*;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactRepository reactRepository;

    @Override
    public Response createPost(MultipartFile[] files, PostRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (files == null && requestDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post is empty");
        }

        if (files != null) {
            checkFileType(files);

            Post post;
            if (requestDTO != null) {
                post = Post.builder()
                        .user(optionalUser.get())
                        .text(requestDTO.getText())
                        .privacy("public")
                        .isDeleted(0)
                        .build();

            } else {
                post = Post.builder()
                        .user(optionalUser.get())
                        .privacy("public")
                        .isDeleted(0)
                        .build();
            }
            postRepository.save(post);
            savePostImage(files, post);
        } else {
            Post post = Post.builder()
                    .user(optionalUser.get())
                    .text(requestDTO.getText())
                    .privacy("public")
                    .isDeleted(0)
                    .build();
            postRepository.save(post);
        }
        return Response.builder()
                .responseMessage("Created post successfully")
                .build();
    }

    private void savePostImage(MultipartFile[] files, Post post) {
        List<PostImage> postImageList = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid.toString();

            try {
                Files.copy(file.getInputStream(), Path.of("C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\" + fileName + ".jpg"));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating post");
            }

            PostImage postImage = PostImage.builder()
                    .filePath("C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\" + fileName)
                    .fileName(fileName)
                    .isDeleted(0)
                    .post(post)
                    .build();
            postImageList.add(postImage);
        });
        postImageRepository.saveAll(postImageList);
    }

    @Override
    public Response editPost(Long postId, MultipartFile[] files, PostRequestDTO requestDTO, PostPrivacyDTO privacyDTO) {

        // check files = null & content = null.
        if (files == null && requestDTO == null) {
            return null;
        }

        // get old post.
        Optional<Post> optionalExistingPost = postRepository.findById(postId);

        if (optionalExistingPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        Post editPost = optionalExistingPost.get();

        // get old post image list -> if exists then soft delete.
        List<PostImage> editPostImageList = postImageRepository.findAllByPostId(postId);
        if (editPostImageList != null) {
            editPostImageList.forEach(postImage -> postImage.setIsDeleted(1));
        }

        // files != null -> save post image.
        if (files != null) {
            checkFileType(files);
            savePostImage(files, editPost);

            // case 1: files != null & content != null
            if (requestDTO != null) {
                editPost.setText(requestDTO.getText());

                // case 2: files != null & content = null -> delete post content.
            } else {
                editPost.setText(null);
            }

            // case 3: files = null & content != null
        } else {
            editPost.setText(requestDTO.getText());
        }

        if (privacyDTO != null) {
            editPost.setPrivacy(privacyDTO.getPrivacy());
        }
        postRepository.save(editPost);

        return Response.builder()
                .responseMessage("Edited post successfully")
                .build();
    }

    private void checkFileType(MultipartFile[] files) {
        Arrays.stream(files).forEach(file -> {
            if (file.getContentType() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose image file(s)");
            }
            MediaType mediaType = MediaType.parseMediaType(file.getContentType());
            if (!mediaType.getType().equals("image")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please choose image file(s)");
            }
        });
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Long> friendIds = friendRepository.findFriendIdsByUserId(user.getId());
        friendIds.remove(user.getId());

        List<Post> allFriendPostList = new ArrayList<>();
        for (Long id : friendIds) {
            List<Post> friendPostList = postRepository.findAllByFriendId(id);
            allFriendPostList.addAll(friendPostList);
        }
        List<Post> userPostList = postRepository.findAllByUserId(user.getId());
        allFriendPostList.addAll(userPostList);

        // sort post by LocalTimeDate
        allFriendPostList.sort(Post::compareTo);

        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();

        for (Post post : allFriendPostList) {
            List<PostImage> postImageList = postImageRepository.findAllByPostId(post.getId());
            List<PostImageDTO> postImageDTOList = convertPostImageToPostImageDTO(postImageList);
            PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                    .user(UserDTO.builder()
                            .id(post.getUser().getId())
                            .username(post.getUser().getUsername())
                            .build())
                    .text(post.getText())
                    .postImageDTOList(postImageDTOList)
                    .build();
            postResponseDTOList.add(postResponseDTO);
        }
        return postResponseDTOList;
    }

    public List<PostImageDTO> convertPostImageToPostImageDTO(List<PostImage> postImageList) {
        List<PostImageDTO> postImageDTOList = new ArrayList<>();

        for (PostImage postImage : postImageList) {
            postImageDTOList.add(PostImageDTO.builder()
                    .filePath(postImage.getFilePath() + ".jpg")
                    .build());
        }
        return postImageDTOList;
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        // get userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // base on postId -> get friendId
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Long friendId = post.getUser().getId();

        if (post.getIsDeleted() == 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        if (!user.getId().equals(friendId)) {
            Friend friend = friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), friendId);
            if (friend == null && !post.getPrivacy().equals("public")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not friends");
            }
            if (post.getPrivacy().equals("only me")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
            }
        }

        return getPostResponseDTO(postId, post, friendId);
    }

    private PostResponseDTO getPostResponseDTO(Long postId, Post post, Long friendId) {
        List<PostImage> postImageList = postImageRepository.findAllByPostId(postId);
        List<PostImageDTO> postImageDTOList = new ArrayList<>();

        for (PostImage postImage : postImageList) {
            postImageDTOList.add(PostImageDTO.builder()
                    .filePath(postImage.getFilePath() + ".jpg")
                    .build());
        }

        return PostResponseDTO.builder()
                .user(UserDTO.builder()
                        .id(friendId)
                        .username(post.getUser().getUsername())
                        .build())
                .text(post.getText())
                .postImageDTOList(postImageDTOList)
                .build();
    }

    @Override
    public Response deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not your post");
        }

        // get comment -> delete if exists
        List<Comment> existComments = commentRepository.findAllByPostId(postId);
        if (existComments != null) {
            for (Comment comment : existComments) {
                comment.setIsDeleted(1);
            }
            commentRepository.saveAll(existComments);
        }

        // get react -> delete if exists
        List<React> existReacts = reactRepository.findAllByPostId(postId);
        if (existReacts != null) {
            for (React react : existReacts) {
                react.setIsDeleted(1);
            }
            reactRepository.saveAll(existReacts);
        }

        // get post image -> delete if exists
        List<PostImage> existPostImages = postImageRepository.findAllByPostId(postId);
        if (existPostImages != null) {
            for (PostImage postImage : existPostImages) {
                postImage.setIsDeleted(1);
            }
            postImageRepository.saveAll(existPostImages);
        }

        post.setIsDeleted(1);
        postRepository.save(post);

        return Response.builder()
                .responseMessage("Post deleted successfully")
                .build();
    }
}
