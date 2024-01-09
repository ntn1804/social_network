package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.LoginRequestDTO;

public interface OtpService {

    String sendOtp(LoginRequestDTO loginRequestDTO);
}
