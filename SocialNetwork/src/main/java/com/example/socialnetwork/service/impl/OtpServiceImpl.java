package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.request.OtpValidationRequest;
import com.example.socialnetwork.dto.response.OtpResponseDTO;
import com.example.socialnetwork.dto.response.TokenResponseDTO;
import com.example.socialnetwork.entity.Otp;
import com.example.socialnetwork.entity.User;
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

        Optional<User> user = userRepository.findByUsername(requestDTO.getUsername());

        if(user.isPresent() && passwordEncoder.matches(requestDTO.getPassword(), user.get().getPassword())) {
            return OtpResponseDTO.builder()
                    .otpCode(generateOtp(requestDTO))
                    .build();
            } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
            }
    }

    public String generateOtp(LoginRequestDTO requestDTO){
        Otp existingOtp = otpRepository.findByUsername(requestDTO.getUsername());
        if (existingOtp != null){
            otpRepository.delete(existingOtp);
        }
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        otpRepository.save(Otp.builder()
                        .username(requestDTO.getUsername())
                        .otpCode(otp)
                        .expired(LocalDateTime.now().plusMinutes(5))
                .build());
        return otp;
    }

    public TokenResponseDTO validateOtp(OtpValidationRequest requestDTO){
        Otp otp = otpRepository.findByUsername(requestDTO.getUsername());
        if(otp == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login to get OTP");
        } else if (otp.getExpired().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expired OTP");
        } else if (!otp.getOtpCode().equals(requestDTO.getOtpCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
        otpRepository.delete(otp);
        return TokenResponseDTO.builder()
                .token(jwtService.generateToken(requestDTO.getUsername()))
                .build();
    }
}
