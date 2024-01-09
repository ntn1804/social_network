package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.LoginRequestDTO;
import com.example.socialnetwork.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String sendOtp(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
           return generateOtp();
        } else {
            return null;
        }
    }

    public String generateOtp(){
        String otp;
        otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        return otp;
    }
}
