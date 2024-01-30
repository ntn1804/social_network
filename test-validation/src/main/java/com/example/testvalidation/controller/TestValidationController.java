package com.example.testvalidation.controller;

import com.example.testvalidation.dto.RegistrationRequestDTO;
import com.example.testvalidation.dto.RegistrationResponseDTO;
import com.example.testvalidation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestValidationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> registration(@Valid @RequestBody RegistrationRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.registerUser(requestDTO));
    }
}
