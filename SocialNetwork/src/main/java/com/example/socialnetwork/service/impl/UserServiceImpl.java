package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.UserConverter;
import com.example.socialnetwork.dto.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.UserRequestDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.JwtService;
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

    @Autowired
    private JwtService jwtService;

    @Override
    public UserRequestDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user = userConverter.toEntity(userRequestDTO);
        user = userRepository.save(user);
        return userConverter.toDto(user);
    }

    public String registerUser(UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findByEmailOrUsername(userRequestDTO.getEmail(), userRequestDTO.getUsername());
        if (userRequestDTO.getUsername() != null && userRequestDTO.getUsername().isEmpty()) {
            return "Username can not be empty";
        } else if (userRequestDTO.getEmail() != null && userRequestDTO.getEmail().isEmpty()) {
            return "Email can not be empty";
        } else if (!patternEmailMatches(userRequestDTO.getEmail(), regexMail)) {
            return "Email is invalid";
        }
        if (existingUser != null) {
            return "Email or Username has been registered";
        } else {
            saveUser(userRequestDTO);
        }
        return "Registered successfully";
    }

    public static boolean patternEmailMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        User existingUser = userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());
        if (null != requestDTO.getEmail() && requestDTO.getEmail().isEmpty()) {
            return "Email can not be empty";
        } else if (null != requestDTO.getUsername() && requestDTO.getUsername().isEmpty()){
            return "Username can not be empty";
        } else if (!patternEmailMatches(requestDTO.getEmail(), regexMail)) {
            return "Invalid email";
        } else if (null == existingUser) {
            return "Invalid email or username 3";
        }
        return "http://localhost:8080/api/v1/user/reset-password\n" + jwtService.generateToken(requestDTO.getUsername());
    }
}
