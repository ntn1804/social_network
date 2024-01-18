package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.ReactRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface ReactService {
    ResponseEntity<Response> reactPost(Long postId, ReactRequestDTO requestDTO);
}
