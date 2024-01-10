package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.LoginRequestDTO;
import com.example.socialnetwork.dto.OtpValidationRequest;
import com.example.socialnetwork.dto.UserRequestDTO;
import com.example.socialnetwork.service.OtpService;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public String registration(@RequestBody UserRequestDTO userRequestDTO) {
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
}
