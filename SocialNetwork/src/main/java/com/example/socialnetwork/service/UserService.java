package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDTO;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    String registerUser(UserDTO userDTO);
}
