package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    Response uploadAvatar(MultipartFile file) throws IOException;

    ResponseEntity<?> showMyAvatar() throws IOException;

    ResponseEntity<?> showOthersAvatar(Long userId) throws IOException;
}
