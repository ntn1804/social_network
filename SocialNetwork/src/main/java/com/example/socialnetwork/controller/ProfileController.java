package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.service.InfoService;
import com.example.socialnetwork.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    @Operation(summary = "Upload my avatar.")
    public ResponseEntity<Response> uploadAvatar(@RequestPart("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadAvatar(file));
    }

    @PostMapping(value = "/info")
    @Operation(summary = "Update my info.")
    public ResponseEntity<UserInfoResponseDTO> updateInfo(@Valid @RequestBody UserInfoRequestDTO requestDTO){
        return ResponseEntity.ok(infoService.updateInfo(requestDTO));
    }

    @GetMapping("/avatar")
    @Operation(summary = "Get my avatar.")
    public ResponseEntity<?> showMyAvatar() throws IOException {
        return imageService.showMyAvatar();
    }

    @GetMapping("/info/{userId}")
    @Operation(summary = "Get others info.")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(infoService.getUserInfo(userId));
    }

    @GetMapping("/info")
    @Operation(summary = "Get my info.")
    public ResponseEntity<UserInfoResponseDTO> getMyInfo(){
        return ResponseEntity.ok(infoService.getMyInfo());
    }
}
