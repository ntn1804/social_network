package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {

    Response comment(Long postId, CommentRequestDTO requestDTO);

    Response editComment(Long commentId, CommentRequestDTO requestDTO);

    List<CommentResponseDTO> getCommentPost(Long postId, int offset, int pageSize);

    Response deleteComment(Long commentId);
}
