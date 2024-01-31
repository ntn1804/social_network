package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.response.ForgotPasswordResponseDTO;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User saveUser(RegistrationRequestDTO requestDTO);
    RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO);
    ForgotPasswordResponseDTO forgotPassword(ForgotPasswordRequestDTO requestDTO);
    Response resetPassword(String tokenResetPassword, ResetPasswordDTO requestDTO);
    Response removeUser();

}
