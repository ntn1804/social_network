package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value ="/create-post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createPost(@RequestPart(value = "post-image", required = false) MultipartFile[] files,
                                               @RequestPart(value = "post-text", required = false) PostRequestDTO requestDTO) throws IOException {
        return ResponseEntity.ok(postService.createPost(files, requestDTO));
    }

    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<Response> editPost(@PathVariable("postId") Long postId,
                                             @RequestPart(value = "post-image", required = false) MultipartFile[] files,
                                             @RequestParam(value = "post-text", required = false) PostRequestDTO requestDTO,
                                              @RequestParam(value = "post-privacy", required = false) PostPrivacyDTO privacyDTO) {
        return ResponseEntity.ok(postService.editPost(postId, files, requestDTO, privacyDTO));
    }

    @GetMapping("/get-post/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }
}
