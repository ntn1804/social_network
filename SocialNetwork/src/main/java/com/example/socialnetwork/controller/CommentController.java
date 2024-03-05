package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    @Operation(summary = "Comment on a post.")
    public ResponseEntity<Response> comment(@PathVariable ("postId") Long postId,
                                            @RequestBody CommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.comment(postId, requestDTO));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Edit comment.")
    public ResponseEntity<Response> editComment(@PathVariable ("commentId") Long commentId,
                                                @RequestBody CommentRequestDTO requestDTO) {
        return ResponseEntity.ok(commentService.editComment(commentId, requestDTO));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get all comments in a post.")
    public ResponseEntity<List<CommentResponseDTO>> getCommentPost(@PathVariable ("postId") Long postId,
                                                                   @RequestParam (defaultValue = "0") int offset,
                                                                   @RequestParam (defaultValue = "5") int pageSize) {
        return ResponseEntity.ok(commentService.getCommentPost(postId, offset, pageSize));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete selected comment.")
    public ResponseEntity<Response> deleteCommentPost(@PathVariable ("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
