package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.service.InfoService;
import com.example.socialnetwork.service.StorageService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserInfoResponseDTO> updateInfo(@Valid @RequestBody UserInfoRequestDTO requestDTO){
        return ResponseEntity.ok(infoService.updateInfo(requestDTO));
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<Response> uploadImageToFileSystem(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(storageService.uploadImageToFileSystem(file));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        return storageService.downloadImageFromFileSystem(fileName);
    }

    @GetMapping("/get-user-info/{userId}")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(infoService.getUserInfo(userId));
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserInfoResponseDTO> getMyInfo(){
        return ResponseEntity.ok(infoService.getMyInfo());
    }
}
