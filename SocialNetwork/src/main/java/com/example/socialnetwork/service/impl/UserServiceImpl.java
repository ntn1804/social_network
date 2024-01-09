package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.UserConverter;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final static String regexMail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User();
        user = userConverter.toEntity(userDTO);
        user = userRepository.save(user);
        return userConverter.toDto(user);
    }

    public String registerUser(UserDTO userDTO) {
        User existingUser = userRepository.findByEmailOrUsername(userDTO.getEmail(),userDTO.getUsername());
        if(userDTO.getUsername() != null && userDTO.getUsername().isEmpty()){
            return "Username can not be empty!";
        } else if (userDTO.getEmail() != null && userDTO.getEmail().isEmpty()){
            return "Email can not be empty!";
        } else if (!patternEmailMatches(userDTO.getEmail(), regexMail)){
            return "Email is invalid!";
        }
        if(existingUser != null){
            return "Email or Username has been registered!";
        } else {
            saveUser(userDTO);
        }
        return "Registered successfully!";
    }

    public static boolean patternEmailMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
