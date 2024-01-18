package com.example.socialnetwork.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    ResponseEntity<?> uploadImageToFileSystem(MultipartFile file) throws IOException;

    ResponseEntity<?> downloadImageFromFileSystem(String fileName) throws IOException;
}
