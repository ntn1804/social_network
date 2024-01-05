package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toEntity(UserDTO dto){
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setRole("user");
        return entity;
    }

    public UserDTO toDto(User entity){
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}
