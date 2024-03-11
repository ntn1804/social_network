package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.ShowAllPostResponseDTO;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.util.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostResponseDTO createPost(MultipartFile[] files, PostRequestDTO requestDTO, PostStatus postStatus) throws IOException;

    Response editPost(Long postId, MultipartFile[] files, PostRequestDTO requestDTO, PostStatus postStatus);

    PostResponseDTO getPostById(Long postId);

    Response deletePost(Long postId);

    List<PostResponseDTO> getAllPosts(int offset, int pageSize);

    List<PostResponseDTO> getMyPosts(int offset, int pageSize);
}