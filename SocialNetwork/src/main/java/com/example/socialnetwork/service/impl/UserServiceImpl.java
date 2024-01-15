package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.entity.TokenResetPassword;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.ResetPasswordRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final static String regexMail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResetPasswordRepo resetPasswordRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(RegistrationRequestDTO requestDTO) {
        User user = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role("USER")
                .build();
        userRepo.save(user);
    }

    public ResponseEntity<Response> registerUser(RegistrationRequestDTO requestDTO) {
        User existingUser = userRepo.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());
        if (requestDTO.getUsername() != null && requestDTO.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("Username can not be empty")
                    .build());
        } else if (requestDTO.getEmail() != null && requestDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("Email can not be empty")
                    .build());
        } else if (!patternEmailMatches(requestDTO.getEmail(), regexMail)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("Email is invalid")
                    .build());
        }
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("Email or Username has been registered")
                    .build());
        } else {
            saveUser(requestDTO);
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Registered successfully")
                .registrationResponse(RegistrationResponseDTO.builder()
                        .username(requestDTO.getUsername())
                        .email(requestDTO.getEmail())
                        .build())
                .build());
    }

    public static boolean patternEmailMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        User existingUser = userRepo.findByEmail(requestDTO.getEmail());
        if (requestDTO.getEmail() != null && requestDTO.getEmail().isEmpty()) {
            return "Invalid email";
        } else if (!patternEmailMatches(requestDTO.getEmail(), regexMail)) {
            return "Invalid email";
        } else if (existingUser == null) {
            return "Email does not exist";
        }
        String tokenResetPassword = generateToken(requestDTO);
        return "http://localhost:8080/api/v1/user/reset-password\n" + tokenResetPassword;
    }

    public String generateToken(ForgotPasswordRequestDTO requestDTO) {
        TokenResetPassword existingToken = resetPasswordRepo.findByEmail(requestDTO.getEmail());
        if(existingToken != null){
            resetPasswordRepo.delete(existingToken);
        }
        UUID uuid = UUID.randomUUID();
        String tokenResetPassword = uuid.toString();
        resetPasswordRepo.save(TokenResetPassword.builder()
                        .email(requestDTO.getEmail())
                        .token(tokenResetPassword)
                        .expired(LocalDateTime.now().plusMinutes(30))
                .build());
        return tokenResetPassword;
    }

    @Override
    public String resetPassword(ResetPasswordDTO requestDTO) {
        TokenResetPassword token = resetPasswordRepo.findByToken(requestDTO.getToken());
        if(Objects.nonNull(token)){
           User user = userRepo.findByEmail(token.getEmail());
           if(Objects.nonNull(user)){
               user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
               userRepo.save(user);
           }else {
               return "deochat";
           }
        }
        return "Reset password successfully";
    }

    @Override
    public ResponseEntity<Response> removeUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> listUser = userRepo.findByUsername(userDetails.getUsername());
        if(listUser.isPresent()){
            User user = listUser.get();
            Long userId = user.getId();
            userRepo.deleteById(userId);
        } else {
            return ResponseEntity.ok(Response.builder()
                            .statusCode(400)
                            .responseMessage("Removed user unsuccessfully")
                    .build());
        }

        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Removed user successfully")
                .build());
    }
}
