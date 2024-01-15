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
    public ResponseEntity<Response> registration(@RequestBody RegistrationRequestDTO requestDTO) {
        return userService.registerUser(requestDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO requestDTO) {
        return otpService.sendOtp(requestDTO);
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

    @DeleteMapping("/remove-user")
    public ResponseEntity<Response> removeUser(){
        return userService.removeUser();
    }
}
