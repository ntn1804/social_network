package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.request.UserRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserRequestDTO saveUser(UserRequestDTO userRequestDTO);
    ResponseEntity<Response> registerUser(UserRequestDTO userRequestDTO);
    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
    String resetPassword(ResetPasswordDTO requestDTO);
}
