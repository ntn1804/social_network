package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Response> createPost(@RequestPart(value = "image", required = false) MultipartFile[] files,
                                               @RequestParam(value = "text", required = false) PostRequestDTO requestDTO ) throws IOException {
        return ResponseEntity.ok(postService.createPost(files, requestDTO));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> editPost(@PathVariable("postId") Long postId,
                                             @RequestPart(value = "image", required = false) MultipartFile[] files,
                                             @RequestParam(value = "text", required = false) PostRequestDTO requestDTO,
                                              @RequestParam(value = "privacy", required = false) PostPrivacyDTO privacyDTO) {
        return ResponseEntity.ok(postService.editPost(postId, files, requestDTO, privacyDTO));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("timeline")
    public ResponseEntity<List<PostResponseDTO>> timeline(@RequestParam(defaultValue = "0") int offset,
                                                          @RequestParam(defaultValue = "5") int pageSize){
        List<PostResponseDTO> postsWithPagination = postService.findPostsWithPagination(offset, pageSize);
        return ResponseEntity.ok()
                .body(postsWithPagination);
    }
}
