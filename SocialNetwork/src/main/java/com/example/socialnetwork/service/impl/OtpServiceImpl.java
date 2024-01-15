package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.request.OtpValidationRequest;
import com.example.socialnetwork.entity.Otp;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.OtpRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.JwtService;
import com.example.socialnetwork.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public String sendOtp(LoginRequestDTO requestDTO) {
        Optional<User> listUser = userRepo.findByUsername(requestDTO.getUsername());
        if (listUser.isPresent()){
            User existingUser = listUser.get();
            if (existingUser.getUsername().equals(requestDTO.getUsername()) &&
                passwordEncoder.matches(requestDTO.getPassword(), existingUser.getPassword())){
                return generateOtp(requestDTO);
            } else {
                return "Invalid username or password";
            }
        }
        return null;
    }

    public String generateOtp(LoginRequestDTO requestDTO){
        Otp existingOtp = otpRepo.findByUsername(requestDTO.getUsername());
        if (existingOtp != null){
            otpRepo.delete(existingOtp);
        }
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        otpRepo.save(Otp.builder()
                        .username(requestDTO.getUsername())
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
