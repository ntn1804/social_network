package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.*;
import com.example.socialnetwork.dto.response.*;
import com.example.socialnetwork.service.OtpService;
import com.example.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Sign up for a new account.")
    public ResponseEntity<RegistrationResponseDTO> registration(@Valid @RequestBody RegistrationRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.registerUser(requestDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login to get OTP.")
    public ResponseEntity<OtpResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok(otpService.sendOtp(requestDTO));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP to get token.")
    public ResponseEntity<TokenResponseDTO> validateOtpAndGetToken(@RequestBody OtpValidationRequest requestDTO) {
        return ResponseEntity.ok(otpService.validateOtp(requestDTO));
    }

    @PostMapping("/password")
    @Operation(summary = "Send request password token to user.")
    public ResponseEntity<ForgotPasswordResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.forgotPassword(requestDTO));
    }

    @PutMapping("/password/{token}")
    @Operation(summary = "Use provided token to change password.")
    public ResponseEntity<Response> resetPassword(@PathVariable("token") String tokenResetPassword,
                                @RequestBody @Valid ResetPasswordDTO requestDTO) {
        return ResponseEntity.ok(userService.resetPassword(tokenResetPassword, requestDTO));
    }
}
