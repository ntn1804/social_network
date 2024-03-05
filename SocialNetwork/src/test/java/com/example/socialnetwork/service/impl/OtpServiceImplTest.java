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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

        assertThrows(GeneralException.class, () -> {
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

        Random randomNumberMock = Mockito.mock(Random.class);
        when(randomNumberMock.nextInt(999999)).thenReturn(123456);
        // Create instance of DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("000000");

        // Generate OTP
        String otp = decimalFormat.format(randomNumberMock.nextInt(999999));


        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);
        when(passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(null);

        OtpResponseDTO result = otpService.sendOtp(requestDTO);

        assertNotNull(result.getOtpCode());
    }

    @Test
    void testValidateOtp_NotLoginException() {
        OtpValidationRequest requestDTO = new OtpValidationRequest("test", "123456");
        User user = User.builder()
                .username("test")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);

        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(null);

        assertThrows(GeneralException.class, () -> {
            otpService.validateOtp(requestDTO);
        });
    }

    @Test
    void testValidateOtp_InvalidOtp() {
        OtpValidationRequest requestDTO = new OtpValidationRequest(
                "test", "123456");

        Otp otp = Otp.builder()
                .otpCode("123457")
                .build();

        User user = User.builder()
                .username("test")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);

        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(otp);

        assertThrows(GeneralException.class, () -> {
            otpService.validateOtp(requestDTO);
        });
    }

    @Test
    void testValidateOtp_ExpiredOtp() {
        OtpValidationRequest requestDTO = new OtpValidationRequest(
                "test", "123456");

        Otp otp = Otp.builder()
                .otpCode("123456")
                .expired(LocalDateTime.now())
                .build();

        User user = User.builder()
                .username("test")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);

        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(otp);

        assertThrows(GeneralException.class, () -> {
            otpService.validateOtp(requestDTO);
        });
    }

    @Test
    void testValidateOtp_SuccessfulCase() {
        OtpValidationRequest requestDTO = new OtpValidationRequest(
                "test", "123456");

        Otp otp = Otp.builder()
                .otpCode("123456")
                .expired(LocalDateTime.now().plusMinutes(5))
                .build();

        User user = User.builder()
                .username("test")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername(requestDTO.getUsername())).thenReturn(optionalUser);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO("token123");

        when(otpRepository.findByUsername(requestDTO.getUsername())).thenReturn(otp);
        when(jwtService.generateToken(requestDTO.getUsername())).thenReturn("token123");

        TokenResponseDTO result = otpService.validateOtp(requestDTO);

        assertEquals(tokenResponseDTO.getToken(), result.getToken());
    }
}