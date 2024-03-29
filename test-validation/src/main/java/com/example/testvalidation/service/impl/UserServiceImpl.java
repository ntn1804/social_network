package com.example.testvalidation.service.impl;

import com.example.testvalidation.dto.RegistrationRequestDTO;
import com.example.testvalidation.dto.RegistrationResponseDTO;
import com.example.testvalidation.entity.User;
import com.example.testvalidation.repository.UserRepository;
import com.example.testvalidation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
    private final static String regexMail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordRepository passwordRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(RegistrationRequestDTO requestDTO) {
        User user = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword())
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO) {
        User existingUser = userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());

        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Username has been registered");
        }

        var res = saveUser(requestDTO);

        return RegistrationResponseDTO.builder()
                .email(res.getEmail())
                .username(res.getUsername())
                .build();
    }

//    public static boolean patternEmailMatches(String emailAddress, String regexPattern) {
//        return Pattern.compile(regexPattern)
//                .matcher(emailAddress)
//                .matches();
//    }

//    @Override
//    public String forgotPassword(ForgotPasswordRequestDTO requestDTO) {
//        User existingUser = userRepository.findByEmail(requestDTO.getEmail());
//        if (requestDTO.getEmail() != null && requestDTO.getEmail().isEmpty()) {
//            return "Invalid email";
//        } else if (!patternEmailMatches(requestDTO.getEmail(), regexMail)) {
//            return "Invalid email";
//        } else if (existingUser == null) {
//            return "Email does not exist";
//        }
//        String tokenResetPassword = generateToken(requestDTO);
//        return "http://localhost:8080/api/v1/user/reset-password/" + tokenResetPassword;
//    }

//    public String generateToken(ForgotPasswordRequestDTO requestDTO) {
//        TokenResetPassword existingToken = passwordRepository.findByEmail(requestDTO.getEmail());
//        if (existingToken != null) {
//            passwordRepository.delete(existingToken);
//        }
//        UUID uuid = UUID.randomUUID();
//        String tokenResetPassword = uuid.toString();
//        passwordRepository.save(TokenResetPassword.builder()
//                .email(requestDTO.getEmail())
//                .token(tokenResetPassword)
//                .expired(LocalDateTime.now().plusMinutes(30))
//                .build());
//        return tokenResetPassword;
//    }

//    @Override
//    public String resetPassword(String tokenResetPassword, ResetPasswordDTO requestDTO) {
//        TokenResetPassword token = passwordRepository.findByToken(tokenResetPassword);
//        if (Objects.nonNull(token)) {
//            User user = userRepository.findByEmail(token.getEmail());
//            if (Objects.nonNull(user)) {
//                user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
//                userRepository.save(user);
//            } else {
//                return "deochat";
//            }
//        }
//        return "Reset password successfully";
//    }

//    @Override
//    public ResponseEntity<Response> removeUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
//        Optional<User> listUser = userRepository.findByUsername(userDetails.getUsername());
//        if (listUser.isPresent()) {
//            User user = listUser.get();
//            Long userId = user.getId();
//            userRepository.deleteById(userId);
//        } else {
//            return ResponseEntity.ok(Response.builder()
//                    .statusCode(400)
//                    .responseMessage("Removed user unsuccessfully")
//                    .build());
//        }
//
//        return ResponseEntity.ok(Response.builder()
//                .statusCode(200)
//                .responseMessage("Removed user successfully")
//                .build());
//    }
}
