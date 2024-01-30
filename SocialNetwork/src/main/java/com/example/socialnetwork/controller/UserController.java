package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.*;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.dto.response.Response;
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
    public String login(@RequestBody LoginRequestDTO requestDTO) {
        return otpService.sendOtp(requestDTO);
    }

    @PostMapping("/validateOtp")
    public String validateOtpAndGetToken(@RequestBody OtpValidationRequest requestDTO) {
        return otpService.validateOtp(requestDTO);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        return userService.forgotPassword(requestDTO);
    }

    @GetMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String tokenResetPassword,
                                @RequestBody ResetPasswordDTO requestDTO) {
        return userService.resetPassword(tokenResetPassword, requestDTO);
    }

    @DeleteMapping("/remove-user")
    public ResponseEntity<Response> removeUser() {
        return userService.removeUser();
    }

}
