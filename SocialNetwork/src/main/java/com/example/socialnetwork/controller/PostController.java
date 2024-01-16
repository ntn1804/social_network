package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<Response> createPost(@RequestParam("post-image") MultipartFile file,
                                               @RequestParam("post-text") PostRequestDTO requestDTO) {
        return postService.createPost(file, requestDTO);
    }
}
