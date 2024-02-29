package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.response.ForgotPasswordResponseDTO;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.TokenResetPassword;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PasswordRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO) {
        User existingUser = userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());

        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Username has been registered");
        }

        var result = saveUser(requestDTO);

        return RegistrationResponseDTO.builder()
                .email(result.getEmail())
                .username(result.getUsername())
                .build();
    }
    @Override
    public User saveUser(RegistrationRequestDTO requestDTO) {
        User user = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    @Override
    public ForgotPasswordResponseDTO forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        User existingUser = userRepository.findByEmail(requestDTO.getEmail());
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email");
        }
        String tokenResetPassword = generateToken(requestDTO);
        return ForgotPasswordResponseDTO.builder()
                .urlAndTokenResetPassword("http://localhost:8080/api/v1/user/reset-password/" + tokenResetPassword)
                .build();
    }

    public String generateToken(ForgotPasswordRequestDTO requestDTO) {
        TokenResetPassword existingToken = passwordRepository.findByEmail(requestDTO.getEmail());
        if (existingToken != null) {
            passwordRepository.delete(existingToken);
        }
        UUID uuid = UUID.randomUUID();
        String tokenResetPassword = uuid.toString();
        passwordRepository.save(TokenResetPassword.builder()
                .email(requestDTO.getEmail())
                .tokenSeries(tokenResetPassword)
                .expired(LocalDateTime.now().plusMinutes(5))
                .build());
        return tokenResetPassword;
    }

    @Override
    public Response resetPassword(String tokenResetPassword, ResetPasswordDTO requestDTO) {
        TokenResetPassword token = passwordRepository.findByTokenSeries(tokenResetPassword);
        if (tokenResetPassword.isEmpty() || token == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        User user = userRepository.findByEmail(token.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);
        passwordRepository.delete(token);

        return Response.builder()
                .responseMessage("Reset password successfully")
                .build();
    }
}
