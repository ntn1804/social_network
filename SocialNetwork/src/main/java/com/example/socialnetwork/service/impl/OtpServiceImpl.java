package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.LoginRequestDTO;
import com.example.socialnetwork.dto.OtpValidationRequest;
import com.example.socialnetwork.entity.Otp;
import com.example.socialnetwork.repository.OtpRepo;
import com.example.socialnetwork.service.JwtService;
import com.example.socialnetwork.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public String sendOtp(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
           return generateOtp(loginRequestDTO);
        } else {
            return "Invalid username or password";
        }
    }

    public String generateOtp(LoginRequestDTO loginRequestDTO){
        Otp existingOtp = otpRepo.findByUsername(loginRequestDTO.getUsername());
        if (existingOtp != null){
            otpRepo.delete(existingOtp);
        }
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        otpRepo.save(Otp.builder()
                        .username(loginRequestDTO.getUsername())
                        .otpCode(otp)
                        .expired(LocalDateTime.now().plusMinutes(5))
                .build());
        return otp;
    }

    public String validateOtp(OtpValidationRequest otpValidationRequest){
        Otp otp = otpRepo.findByUsername(otpValidationRequest.getUsername());
        if(otp == null){
            return "Login to get OTP";
        } else if (otp.getExpired().isBefore(LocalDateTime.now())) {
            return "Expired OTP";
        } else if (!otp.getOtpCode().equals(otpValidationRequest.getOtpCode())){
            return "Invalid OTP";
        }
        return jwtService.generateToken(otpValidationRequest.getUsername());
    }
}
