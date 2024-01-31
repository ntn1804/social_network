package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    Response uploadImageToFileSystem(MultipartFile file) throws IOException;

    ResponseEntity<?> downloadImageFromFileSystem(String fileName) throws IOException;
}
