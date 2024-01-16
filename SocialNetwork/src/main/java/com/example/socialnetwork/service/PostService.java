package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    ResponseEntity<Response> createPost(MultipartFile file, PostRequestDTO requestDTO);
}
