package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostImageDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.*;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.util.FileUtils;
import com.example.socialnetwork.util.PostStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public PostResponseDTO createPost(MultipartFile[] files, PostRequestDTO requestDTO, PostStatus postStatus) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        if (files == null && requestDTO == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Post is empty");
        }

        if (files != null) {
            checkFileType(files);

            Post post;
            if (requestDTO != null) {
                post = Post.builder()
                        .user(user)
                        .text(requestDTO.getText())
                        .postStatus(postStatus)
                        .isDeleted(0)
                        .build();

            } else {
                post = Post.builder()
                        .user(user)
                        .postStatus(postStatus)
                        .isDeleted(0)
                        .build();
            }
            postRepository.save(post);
            savePostImage(files, post);

            // mapping postResponseDTO
            postResponseDTO = getPostResponseDTO(post);
        }

        if (files == null) {
            if (requestDTO.getText().isEmpty()) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "Post is empty");
            }
            Post post = Post.builder()
                    .user(user)
                    .text(requestDTO.getText())
                    .postStatus(postStatus)
                    .isDeleted(0)
                    .build();
            postRepository.save(post);

            // mapping postResponseDTO
            postResponseDTO = getPostResponseDTO(post);
        }
        return postResponseDTO;
    }

    private void savePostImage(MultipartFile[] multipartFiles, Post post) {
        List<PostImage> postImageList = new ArrayList<>();
        Path folderPath = Paths.get("src/main/resources/static/images/post");
        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            try {
                Path filePath = FileUtils.imageUploadUtil(multipartFile, folderPath);
                PostImage postImage = PostImage.builder()
                        .filePath(filePath.toFile().getPath())
                        .fileName(filePath.toFile().getName())
                        .isDeleted(0)
                        .post(post)
                        .build();
                postImageList.add(postImage);
            } catch (IOException e) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "Error creating post");
            }
        });
        postImageRepository.saveAll(postImageList);
    }

    @Override
    public Response editPost(Long postId, MultipartFile[] files, PostRequestDTO requestDTO, PostStatus postStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser.orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        // get old post.
        Optional<Post> optionalExistingPost = postRepository.findById(postId);
        Post post = optionalExistingPost
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Post not found"));

        // not your post.
        if (!user.getId().equals(post.getUser().getId())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Can not edit other posts");
        }

        // deleted post.
        if (post.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        // check files = null & content = null.
        if (files == null && requestDTO == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Post is empty");
        }

        // get old post image list -> if exists then soft delete.
        List<PostImage> oldPostImageList = postImageRepository.findAllByPostId(postId);
        if (oldPostImageList != null) {
            oldPostImageList.forEach(postImage -> postImage.setIsDeleted(1));
        }

        // check files
        if (files != null) {
            checkFileType(files);
            savePostImage(files, post);
        }

        // check requestDTO
        if (requestDTO != null) {
            post.setText(requestDTO.getText());
        }
        if (requestDTO == null || requestDTO.getText().isEmpty()) {
            post.setText(null);
        }

        // save post status change
        post.setPostStatus(postStatus);

        postRepository.save(post);

        return Response.builder()
                .responseMessage("Edited post successfully")
                .build();
    }

    private void checkFileType(MultipartFile[] multipartFiles) {
        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            if (multipartFile.getContentType() == null) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "please choose file(s)");
            }
            MediaType mediaType = MediaType.parseMediaType(multipartFile.getContentType());
            if (!mediaType.getType().equals("image")) {
                throw new InvalidMediaTypeException(multipartFile.getContentType(), "please choose image file(s)");
            }
        });
    }

    @Override
    public List<PostResponseDTO> getAllPosts(int offset, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser.orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));
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
                    .postId(post.getId())
                    .username(post.getUser().getUsername())
                    .postContent(post.getText())
                    .postImages(postImageDTOList)
                    .build();
            postResponseDTOList.add(postResponseDTO);
        }

        Pageable pageRequest = PageRequest.of(offset, pageSize);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), postResponseDTOList.size());

        return postResponseDTOList.subList(start, end);
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
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        // base on postId -> get friendId
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Post not found"));
        Long friendId = post.getUser().getId();

        if (post.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        if (!user.getId().equals(friendId)) {
            Friend friend = friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), friendId);
            if (friend == null && !post.getPostStatus().equals(PostStatus.PUBLIC)) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "You are not friends");
            }
            if (post.getPostStatus() == PostStatus.PRIVATE) {
                throw new GeneralException(HttpStatus.NOT_FOUND, "Post not found");
            }
        }

        return getPostResponseDTO(post);
    }

    private PostResponseDTO getPostResponseDTO(Post post) {
        List<PostImage> postImageList = postImageRepository.findAllByPostId(post.getId());
        List<PostImageDTO> postImageDTOList = new ArrayList<>();

        for (PostImage postImage : postImageList) {
            postImageDTOList.add(PostImageDTO.builder()
                    .filePath(postImage.getFilePath())
                    .build());
        }

        return PostResponseDTO.builder()
                .postId(post.getId())
                .username(post.getUser().getUsername())
                .postStatus(post.getPostStatus())
                .postContent(post.getText())
                .postImages(postImageDTOList)
                .build();
    }

    @Override
    public Response deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Post not found"));

        if (post.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post not found");
        }

        if (!post.getUser().getId().equals(user.getId())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Not your post");
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

    @Override
    public List<PostResponseDTO> getMyPosts(int offset, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        List<Post> postList = postRepository.findAllByUserId(user.getId());
        postList.sort(Post::compareTo);

        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post post : postList) {
            List<PostImage> postImageList = postImageRepository.findAllByPostId(post.getId());
            List<PostImageDTO> postImageDTO = convertPostImageToPostImageDTO(postImageList);
            PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                    .postId(post.getId())
                    .username(user.getUsername())
                    .postContent(post.getText())
                    .postImages(postImageDTO)
                    .build();
            postResponseDTOList.add(postResponseDTO);
        }

        Pageable pageRequest = PageRequest.of(offset, pageSize);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), postResponseDTOList.size());

        return postResponseDTOList.subList(start, end);
    }
}
