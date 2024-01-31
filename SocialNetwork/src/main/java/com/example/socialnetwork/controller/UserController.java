package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.*;
import com.example.socialnetwork.dto.response.*;
import com.example.socialnetwork.service.OtpService;
import com.example.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> registration(@Valid @RequestBody RegistrationRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.registerUser(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<OtpResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok(otpService.sendOtp(requestDTO));
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<TokenResponseDTO> validateOtpAndGetToken(@RequestBody OtpValidationRequest requestDTO) {
        return ResponseEntity.ok(otpService.validateOtp(requestDTO));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.forgotPassword(requestDTO));
    }

    @GetMapping("/reset-password/{token}")
    public ResponseEntity<Response> resetPassword(@PathVariable("token") String tokenResetPassword,
                                @RequestBody ResetPasswordDTO requestDTO) {
        return ResponseEntity.ok(userService.resetPassword(tokenResetPassword, requestDTO));
    }

    @DeleteMapping("/remove-user")
    public ResponseEntity<Response> removeUser() {
        return ResponseEntity.ok(userService.removeUser());
    }
}
