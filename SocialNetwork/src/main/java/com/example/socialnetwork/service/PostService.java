package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {
    ResponseEntity<Response> createPost(MultipartFile file, PostRequestDTO requestDTO) throws IOException;

    ResponseEntity<Response> editPost(Long postId, MultipartFile file, PostRequestDTO requestDTO);

    ResponseEntity<Response> getFriendsPost();
}
