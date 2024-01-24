package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Comment;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.CommentRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ResponseEntity<Response> comment(Long postId, CommentRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (postId != null) {
            Optional<Post> post = postRepository.findById(postId);
            if(requestDTO != null && requestDTO.getContent().isEmpty()){
                return ResponseEntity.ok(Response.builder()
                        .statusCode(400)
                        .responseMessage("Comment is empty")
                        .build());
            } else {
                Comment comment = Comment.builder()
                        .content(requestDTO != null ? requestDTO.getContent() : null)
                        .user(user.orElse(null))
                        .post(post.orElse(null))
                        .build();
                commentRepository.save(comment);
            }

        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("OK")
                .commentResponse(CommentResponseDTO.builder()
                        .content(requestDTO != null ? requestDTO.getContent() : null)
                        .build())
                .build());
    }

    @Override
    public ResponseEntity<Response> editComment(Long commentId, CommentRequestDTO requestDTO) {
        Optional<Comment> oldComment = commentRepository.findById(commentId);
        
        if(requestDTO != null && requestDTO.getContent().isEmpty()) {
            return ResponseEntity.ok(Response.builder()
                    .statusCode(400)
                    .responseMessage("Comment is empty")
                    .build());
        }

        if(oldComment.isPresent()){
            Comment newComment = oldComment.get();
            newComment.setContent(requestDTO != null ? requestDTO.getContent() : null);
            commentRepository.save(newComment);
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("OK")
                .commentResponse(CommentResponseDTO.builder()
                        .content(requestDTO != null ? requestDTO.getContent() : null)
                        .build())
                .build());
    }
}
