package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.ShowAllPostResponseDTO;
import com.example.socialnetwork.entity.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Response createPost(MultipartFile file, PostRequestDTO requestDTO) throws IOException;

    ResponseEntity<Response> editPost(Long postId, MultipartFile file, PostRequestDTO requestDTO);

    ResponseEntity<List<ShowAllPostResponseDTO>> getAllPosts();

}