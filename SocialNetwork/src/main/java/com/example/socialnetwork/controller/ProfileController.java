package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.UserRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.StorageService;
import com.example.socialnetwork.service.impl.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private StorageService storageService;

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        return storageService.uploadImage(file);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        return storageService.downloadImage(fileName);
    }
}
