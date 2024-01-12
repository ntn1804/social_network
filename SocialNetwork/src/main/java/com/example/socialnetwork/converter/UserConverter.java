package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.request.UserRequestDTO;
import com.example.socialnetwork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User toEntity(UserRequestDTO dto){
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRole("USER");
        return entity;
    }

    public UserRequestDTO toDto(User entity){
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}
