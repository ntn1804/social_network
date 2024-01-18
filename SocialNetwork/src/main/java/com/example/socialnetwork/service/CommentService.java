package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<Response> comment(Long postId, CommentRequestDTO requestDTO);

    ResponseEntity<Response> editComment(Long commentId, CommentRequestDTO requestDTO);
}
