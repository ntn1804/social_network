package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.*;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.OtpService;
import com.example.socialnetwork.service.UserService;
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
    public ResponseEntity<Response> registration(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.registerUser(userRequestDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return otpService.sendOtp(loginRequestDTO);
    }

    @PostMapping("/validateOtp")
    public String validateOtpAndGetToken(@RequestBody OtpValidationRequest otpValidationRequest) {
        return otpService.validateOtp(otpValidationRequest);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO){
        return userService.forgotPassword(requestDTO);
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordDTO requestDTO){
        return userService.resetPassword(requestDTO);
    }
}
