package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment-post/{postId}")
    public ResponseEntity<Response> comment(@PathVariable ("postId") Long postId,
                                            @RequestBody CommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.comment(postId, requestDTO));
    }

    @PutMapping("/edit-comment/{commentId}")
    public ResponseEntity<Response> editComment(@PathVariable ("commentId") Long commentId,
                                                @RequestBody CommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.editComment(commentId, requestDTO));
    }

    @GetMapping("/get-comment/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentPost(@PathVariable ("postId") Long postId) {
        return ResponseEntity.ok(commentService.getCommentPost(postId));
    }
}
