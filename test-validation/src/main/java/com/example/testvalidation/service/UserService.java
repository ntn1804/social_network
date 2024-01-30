package com.example.testvalidation.service;

import com.example.testvalidation.dto.RegistrationRequestDTO;
import com.example.testvalidation.dto.RegistrationResponseDTO;
import com.example.testvalidation.entity.User;

public interface UserService {
    User saveUser(RegistrationRequestDTO requestDTO);
    RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO);
//    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
//    String resetPassword(String tokenResetPassword, ResetPasswordDTO requestDTO);
//    ResponseEntity<Response> removeUser();

}
