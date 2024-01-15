package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    void saveUser(RegistrationRequestDTO requestDTO);
    ResponseEntity<Response> registerUser(RegistrationRequestDTO requestDTO);
    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
    String resetPassword(ResetPasswordDTO requestDTO);
    ResponseEntity<Response> removeUser();
}
