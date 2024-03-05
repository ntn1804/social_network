package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserDTO;
import com.example.socialnetwork.entity.Comment;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.CommentRepository;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Override
    public Response comment(Long postId, CommentRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        // check post exists
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Post not found"));

        // check post deleted
        if (post.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        if (post.getPrivacy().equals("only me")) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post not found");
        }

        // check friend
        if (!user.getId().equals(post.getUser().getId())) {
            Friend friend = friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId());
            if (friend == null) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "You are not friends");
            }
        }

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(requestDTO.getContent())
                .isDeleted(0)
                .build();
        commentRepository.save(comment);

        return Response.builder()
                .commentId(comment.getId())
                .comment(requestDTO.getContent())
                .build();
    }

    @Override
    public Response editComment(Long commentId, CommentRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Comment not found"));

        // check post exists
        if (comment.getPost().getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        // check whose comment
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Not your comment");
        }

        // check empty comment
        if (requestDTO.getContent().isEmpty()) {
            throw new GeneralException(HttpStatus.NO_CONTENT, "Comment is blank");
        }

        comment.setContent(requestDTO.getContent());
        commentRepository.save(comment);

        return Response.builder()
                .commentId(comment.getId())
                .comment("Comment edited: " + requestDTO.getContent())
                .build();
    }

    @Override
    public List<CommentResponseDTO> getCommentPost(Long postId, int offset, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        // check post exists
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Post not found"));

        // check post is deleted
        if (post.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post not found");
        }

        // check friends
        if (!user.getId().equals(post.getUser().getId())) {
            Friend friend = friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId());
            if (friend == null) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "You are not friends");
            }
        }

        List<Comment> commentList = commentRepository.findAllByPostId(postId);

        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                    .username(comment.getUser().getUsername())
                    .comment(comment.getContent())
                    .build();
            commentResponseDTOList.add(commentResponseDTO);
        }

        Pageable pageRequest = PageRequest.of(offset, pageSize);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), commentResponseDTOList.size());

        return commentResponseDTOList.subList(start, end);
    }

    @Override
    public Response deleteComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (comment.getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Comment is deleted");
        }

        if (comment.getPost().getIsDeleted() == 1) {
            throw new GeneralException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Not your comment");
        }
        commentRepository.delete(comment);

        return Response.builder()
                .responseMessage("Deleted comment successfully")
                .build();
    }
}
