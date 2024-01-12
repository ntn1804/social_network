package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.request.OtpValidationRequest;

public interface OtpService {

    String sendOtp(LoginRequestDTO loginRequestDTO);

    String validateOtp(OtpValidationRequest otpValidationRequest);
}
