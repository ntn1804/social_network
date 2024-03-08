package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.request.ResetPasswordDTO;
import com.example.socialnetwork.dto.response.ForgotPasswordResponseDTO;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.TokenResetPassword;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.PasswordRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import com.example.socialnetwork.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageRepository imageRepository;

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO) throws IOException {
        User existingUser = userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());

        if (existingUser != null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Email or Username has been registered");
        }

        // Set default avatar
        Path filePath = saveDefaultAvatar();

        // Save user
        User user = saveUser(requestDTO);

        imageRepository.save(Avatar.builder()
                .name(filePath.getFileName().toString())
                .filePath(filePath.toFile().getPath())
                .isDeleted(0)
                .user(user)
                .note("default avatar")
                .build());

        return RegistrationResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public Path saveDefaultAvatar() throws IOException {
        Path path = Paths.get("src/main/resources/static/images/default-avatar.jpg");
        String name = "default-avatar.jpg";
        String originalFileName = "default-avatar.jpg";
        String contentType = "image/jpeg";
        byte[] content = Files.readAllBytes(path);

        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

        return FileUtils.imageUploadUtil(multipartFile);
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
            throw new GeneralException(HttpStatus.NOT_FOUND, "Invalid email");
        }
        String tokenResetPassword = generateToken(requestDTO);
        return ForgotPasswordResponseDTO.builder()
                .urlAndTokenResetPassword("http://localhost:8080/api/v1/user/password/" + tokenResetPassword)
                .build();
    }

    public String generateToken(ForgotPasswordRequestDTO requestDTO) {
        TokenResetPassword existingToken = passwordRepository.findByEmail(requestDTO.getEmail());
        if (existingToken != null) {
            UUID uuid = UUID.randomUUID();
            String tokenResetPassword = uuid.toString();
            existingToken.setTokenSeries(tokenResetPassword);
            passwordRepository.save(existingToken);
            return tokenResetPassword;
        }
        UUID uuid = UUID.randomUUID();
        String tokenResetPassword = uuid.toString();
        passwordRepository.save(TokenResetPassword.builder()
                .email(requestDTO.getEmail())
                .tokenSeries(tokenResetPassword)
                .expired(LocalDateTime.now())
                .build());
        return tokenResetPassword;
    }

    @Override
    public Response resetPassword(String tokenResetPassword, ResetPasswordDTO requestDTO) {
        TokenResetPassword token = passwordRepository.findByTokenSeriesAndEmail(tokenResetPassword, requestDTO.getEmail());
        if (tokenResetPassword.isEmpty() || token == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Invalid email or token");
        }

        if (token.getExpired().isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        User user = userRepository.findByEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);
        passwordRepository.delete(token);

        return Response.builder()
                .responseMessage("Reset password successfully")
                .build();
    }
}
