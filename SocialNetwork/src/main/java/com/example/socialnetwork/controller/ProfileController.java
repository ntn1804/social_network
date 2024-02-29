package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.service.InfoService;
import com.example.socialnetwork.service.ImageService;
import jakarta.validation.Valid;
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
    private ImageService imageService;

    @Autowired
    private InfoService infoService;

    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> uploadAvatar(@RequestPart("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadAvatar(file));
    }

    @PostMapping("/update-info")
    public ResponseEntity<UserInfoResponseDTO> updateInfo(@Valid @RequestBody UserInfoRequestDTO requestDTO){
        return ResponseEntity.ok(infoService.updateInfo(requestDTO));
    }

    @GetMapping("/my-avatar")
    public ResponseEntity<?> showMyAvatar() throws IOException {
        return imageService.showMyAvatar();
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
