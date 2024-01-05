package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.entity.User;

public interface IUserService {
    UserDTO saveUser(UserDTO userDTO);

    String registration(UserDTO userDTO);
}
