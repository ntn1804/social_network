package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.LoginRequestDTO;
import com.example.socialnetwork.dto.response.OtpResponseDTO;
import com.example.socialnetwork.entity.Otp;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.OtpRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.util.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {
    @InjectMocks
    private OtpServiceImpl otpService;
    @Mock
    private OtpRepository otpRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;

    @Test
    void testSendOtp_PasswordNotMatch() {
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

        User user = User.builder()
                .password("testPassword")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);
        when(passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
                .thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            otpService.sendOtp(requestDTO);
        });
    }

    @Test
    void testSendOtp_PasswordMatch() {
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

        User user = User.builder()
                .password("testPassword")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        final DecimalFormat mockFormatter = mock(DecimalFormat.class);

        Random randomNumberMock = Mockito.mock(Random.class);
        when(randomNumberMock.nextInt(999999)).thenReturn(123456);
        // Create instance of DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("000000");

        // Generate OTP
        String otp = decimalFormat.format(randomNumberMock.nextInt(999999));

//        final StringBuffer buffer = new StringBuffer("123456");
//        when(mockFormatter.format(anyInt())).thenReturn(buffer.toString());
//        when(mockFormatter.format(anyLong(), any(StringBuffer.class), any(FieldPosition.class)))
//                .thenReturn(buffer);

//        String otpCode = "123456";
//
//        Otp otp = Otp.builder()
//                .otpCode(otpCode)
//                .build();

        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);
        when(passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(null);

//        String otpCode = otpService.generateOtp(requestDTO);

        OtpResponseDTO result = otpService.sendOtp(requestDTO);

//        OtpResponseDTO.builder()
//                .otpCode(otp.getOtpCode())
//                .build();

//        assertEquals(otp, result.getOtpCode());
        assertNotNull(result.getOtpCode());
    }
}