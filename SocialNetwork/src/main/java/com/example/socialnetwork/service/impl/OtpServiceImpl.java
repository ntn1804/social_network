package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.request.OtpValidationRequest;
import com.example.socialnetwork.dto.response.OtpResponseDTO;
import com.example.socialnetwork.dto.response.TokenResponseDTO;
import com.example.socialnetwork.entity.Otp;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.OtpRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.util.JwtService;
import com.example.socialnetwork.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OtpResponseDTO sendOtp(LoginRequestDTO requestDTO) {

        Optional<User> optionalUser = userRepository.findByUsername(requestDTO.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        if(passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            return OtpResponseDTO.builder()
                    .otpCode(generateOtp(requestDTO))
                    .build();
            } else {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Invalid username or password");
            }
    }

    public String generateOtp(LoginRequestDTO requestDTO){
        Otp existingOtp = otpRepository.findByUsername(requestDTO.getUsername());
        if (existingOtp != null){
            String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
            existingOtp.setOtpCode(otp);
            existingOtp.setExpired(LocalDateTime.now());
            otpRepository.save(existingOtp);
            return otp;
        }
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        otpRepository.save(Otp.builder()
                        .username(requestDTO.getUsername())
                        .otpCode(otp)
                        .expired(LocalDateTime.now())
                .build());
        return otp;
    }

    public TokenResponseDTO validateOtp(OtpValidationRequest requestDTO){
        Optional<User> optionalUser = userRepository.findByUsername(requestDTO.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, "User not found"));

        Otp otp = otpRepository.findByUsername(requestDTO.getUsername());
        if(otp == null){
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Login to get OTP");
        }
        if (!otp.getOtpCode().equals(requestDTO.getOtpCode())){
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
        if (otp.getExpired().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Expired OTP");
        }
        otpRepository.delete(otp);
        return TokenResponseDTO.builder()
                .token(jwtService.generateToken(requestDTO.getUsername()))
                .build();
    }
}
