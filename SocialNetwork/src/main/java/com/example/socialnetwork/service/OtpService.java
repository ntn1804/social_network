package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.request.OtpValidationRequest;
import com.example.socialnetwork.dto.response.OtpResponseDTO;
import com.example.socialnetwork.dto.response.TokenResponseDTO;

public interface OtpService {

    OtpResponseDTO sendOtp(LoginRequestDTO requestDTO);

    TokenResponseDTO validateOtp(OtpValidationRequest requestDTO);
}
