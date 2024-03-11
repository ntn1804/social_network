package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.util.PostStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a post.")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@RequestPart(value = "image", required = false) MultipartFile[] files,
                                      @RequestParam(value = "text", required = false) PostRequestDTO requestDTO,
                                      @RequestParam("postStatus") PostStatus postStatus) throws IOException {
        return postService.createPost(files, requestDTO, postStatus);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Edit a post.")
    public ResponseEntity<Response> editPost(@PathVariable("postId") Long postId,
                                             @RequestPart(value = "image", required = false) MultipartFile[] files,
                                             @RequestParam(value = "text", required = false) PostRequestDTO requestDTO,
                                             @RequestParam("postStatus") PostStatus postStatus) {
        return ResponseEntity.ok(postService.editPost(postId, files, requestDTO, postStatus));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get a post by Id.")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post.")
    public ResponseEntity<Response> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @GetMapping("/timeline")
    @Operation(summary = "Get all posts.")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(@RequestParam(defaultValue = "0") int offset,
                                                             @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.ok(postService.getAllPosts(offset, pageSize));
    }

    @GetMapping("/my-posts")
    @Operation(summary = "Get my posts.")
    public ResponseEntity<List<PostResponseDTO>> getMyPosts(@RequestParam(defaultValue = "0") int offset,
                                                             @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.ok(postService.getMyPosts(offset, pageSize));
    }
}
