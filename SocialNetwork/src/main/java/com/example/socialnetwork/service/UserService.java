package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.ResetPasswordDTO;
import com.example.socialnetwork.dto.UserRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserRequestDTO saveUser(UserRequestDTO userRequestDTO);
    ResponseEntity<Response> registerUser(UserRequestDTO userRequestDTO);
    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
    String resetPassword(ResetPasswordDTO requestDTO);
}
