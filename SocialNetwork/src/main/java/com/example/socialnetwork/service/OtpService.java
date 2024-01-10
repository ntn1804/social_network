package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.LoginRequestDTO;
import com.example.socialnetwork.dto.OtpValidationRequest;

public interface OtpService {

    String sendOtp(LoginRequestDTO loginRequestDTO);

    String validateOtp(OtpValidationRequest otpValidationRequest);
}
