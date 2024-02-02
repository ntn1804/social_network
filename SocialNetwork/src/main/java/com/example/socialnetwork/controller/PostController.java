package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.ShowAllPostResponseDTO;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<Response> createPost(@RequestParam(value = "post-image",
                                                    required = false) MultipartFile[] files,
                                               @RequestParam(value = "post-text",
                                                       required = false) PostRequestDTO requestDTO) throws IOException {
        return ResponseEntity.ok(postService.createPost(files, requestDTO));
    }

    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<Response> editPost(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "post-image", required = false) MultipartFile file,
                                             @RequestParam(value = "post-text", required = false) PostRequestDTO requestDTO) {
        return postService.editPost(postId, file, requestDTO);
    }

    @PutMapping("/edit-post2/{postId}")
    public ResponseEntity<Response> editPost2(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "post-image", required = false) MultipartFile[] files,
                                             @RequestParam(value = "post-text", required = false) PostRequestDTO requestDTO) {
        return ResponseEntity.ok(postService.editPost2(postId, files, requestDTO));
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<ShowAllPostResponseDTO>> getAllPosts(){
        return postService.getAllPosts();
    }
}
