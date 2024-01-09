package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.LoginRequestDTO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.service.OtpService;
import com.example.socialnetwork.service.UserService;
import com.example.socialnetwork.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public String registration(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(loginRequestDTO.getUsername());
        }else{
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO){
        return otpService.sendOtp(loginRequestDTO);
    }
}
