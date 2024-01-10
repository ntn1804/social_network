package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.UserRequestDTO;

public interface UserService {
    UserRequestDTO saveUser(UserRequestDTO userRequestDTO);
    String registerUser(UserRequestDTO userRequestDTO);
    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
}
