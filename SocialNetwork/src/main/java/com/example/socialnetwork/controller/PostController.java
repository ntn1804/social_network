package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<Response> createPost(@RequestParam(value = "post-image",
                                                        required = false) MultipartFile file,
                                               @RequestParam(value = "post-text",
                                                       required = false) PostRequestDTO requestDTO) throws IOException {
        return postService.createPost(file, requestDTO);
    }
    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<Response> editPost(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "post-image", required = false) MultipartFile file,
                                             @RequestParam(value = "post-text", required = false) PostRequestDTO requestDTO) throws IOException {
        return postService.editPost(postId, file, requestDTO);
    }

    @GetMapping("/get-post")
    public ResponseEntity<Response> getFriendsPost(){
        return postService.getFriendsPost();
    }
}
