package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.InfoService;
import com.example.socialnetwork.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private InfoService infoService;

    @PostMapping("/update-info")
    public ResponseEntity<Response> updateInfo(@RequestBody UserInfoRequestDTO requestDTO){
        return infoService.updateInfo(requestDTO);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image") MultipartFile file) throws IOException {
        return storageService.uploadImageToFileSystem(file);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        return storageService.downloadImageFromFileSystem(fileName);
    }

    @GetMapping("/get-user-info/{userId}")
    public ResponseEntity<Response> getUserInfo(@PathVariable("userId") Long userId){
        return infoService.getUserInfo(userId);
    }
}
