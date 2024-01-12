package com.example.socialnetwork.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    ResponseEntity<?> uploadImage(MultipartFile file) throws IOException;

    ResponseEntity<?> downloadImage(String fileName);
}
