package com.example.socialnetwork.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordRepository passwordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_ExistingUser() {
        // Arrange
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(
                "test@gmail.com",
                "test",
                "1234");

        User existingUser = new User();
        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setUsername(requestDTO.getUsername());

        when(userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername()))
                .thenReturn(existingUser);

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> {
            userService.registerUser(requestDTO);
        });

        verify(userRepository).findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_UniqueUser() {
        // Given
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(
                "test@gmail.com",
                "test",
                "1234");

        when(userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername()))
                .thenReturn(null);

        User savedUser = new User();
        savedUser.setEmail(requestDTO.getEmail());
        savedUser.setUsername(requestDTO.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        RegistrationResponseDTO responseDTO = userService.registerUser(requestDTO);

        // Then
        verify(userRepository).findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername());
        verify(userRepository).save(any(User.class));

        assertEquals(responseDTO.getEmail(), requestDTO.getEmail());
        assertEquals(responseDTO.getUsername(), requestDTO.getUsername());
    }

    @Test
    void shouldSaveUserAndReturnSavedUser() {
        // Given
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(
                "test@gmail.com",
                "test",
                "1234");

        User savedUser = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password("encodedPassword")
                .role("USER")
                .build();

        // Mock the behavior of the userRepository.save method to return the savedUser
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Mock the behavior of the passwordEncoder.encode method to return the encoded password
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");

        // When
        User result = userService.saveUser(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(requestDTO.getEmail(), result.getEmail());
        assertEquals(requestDTO.getUsername(), result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("USER", result.getRole());

        // Verify that the userRepository.save method was called with the correct user object
        verify(userRepository).save(argThat(user -> {
            assertEquals(requestDTO.getEmail(), user.getEmail());
            assertEquals(requestDTO.getUsername(), user.getUsername());
            assertEquals("encodedPassword", user.getPassword());
            assertEquals("USER", user.getRole());
            return true;
        }));
    }

    @Test
    void testForgotPassword_InvalidEmail() {
        // Given
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("test@gmail.com");

        when(userRepository.findByEmail(requestDTO.getEmail()))
                .thenReturn(null);

        // When
        assertThrows(ResponseStatusException.class, () -> {
            userService.forgotPassword(requestDTO);
        });
    }

    @Test
    void testForgotPassword_ValidEmail() {
        //Given
        UUID uuidTest = UUID.fromString("df7dcce6-3495-49e2-aa01-c649647865d9");
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("test@gmail.com");

        User existingUser = new User();
        existingUser.setEmail(requestDTO.getEmail());

        try (MockedStatic<UUID> utilities = Mockito.mockStatic(UUID.class)) {
            // xu ly random cua UUID
            utilities.when(UUID::randomUUID).thenReturn(uuidTest);

            TokenResetPassword existingToken = TokenResetPassword.builder()
                    .email("emailTest")
                    .tokenSeries(uuidTest.toString())
                    .expired(LocalDateTime.now())
                    .build();

            // When
            when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(existingUser);

            when(passwordRepository.save(ArgumentMatchers.nullable(TokenResetPassword.class)))
                    .thenReturn(existingToken);

            String tokenResetPassword = userService.generateToken(ForgotPasswordRequestDTO.builder()
                    .email("emailTest")
                    .build());

            ForgotPasswordResponseDTO result = userService.forgotPassword(requestDTO);

            // Then

            assertEquals("http://localhost:8080/api/v1/user/password/" + tokenResetPassword,
                result.getUrlAndTokenResetPassword());
        }
        verify(passwordRepository,times(0)).delete(any());
    }

    @Test
    void testResetPassword_InvalidToken() {
        ResetPasswordDTO requestDTO = new ResetPasswordDTO("test@gmail.com", "1234");

        String tokenResetPassword = "dc51e081-8d7d-49a0-9283-e0e1d0935c71";

        when(passwordRepository.findByTokenSeriesAndEmail(tokenResetPassword, requestDTO.getEmail())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            userService.resetPassword(tokenResetPassword, requestDTO);
        });
    }

    @Test
    void testResetPassword_ExpiredToken() {
        ResetPasswordDTO requestDTO = new ResetPasswordDTO("test@gmail.com", "1234");

        String tokenResetPassword = "dc51e081-8d7d-49a0-9283-e0e1d0935c71";

        TokenResetPassword token = TokenResetPassword.builder()
                .email("test@gmail.com")
                .expired(LocalDateTime.now().minusMinutes(5))
                .build();

        when(passwordRepository.findByTokenSeriesAndEmail(tokenResetPassword, requestDTO.getEmail())).thenReturn(token);

        assertThrows(ResponseStatusException.class, () -> {
            userService.resetPassword(tokenResetPassword, requestDTO);
        });
    }

    @Test
    void testResetPassword_ValidToken() {
        ResetPasswordDTO requestDTO = new ResetPasswordDTO("test@gmail.com", "1234");

        String tokenResetPassword = "dc51e081-8d7d-49a0-9283-e0e1d0935c71";

        TokenResetPassword token = TokenResetPassword.builder()
                .email("test@gmail.com")
                .expired(LocalDateTime.now().plusMinutes(5))
                .build();

        User user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode(requestDTO.getNewPassword()))
                .build();

        when(passwordRepository.findByTokenSeriesAndEmail(tokenResetPassword, requestDTO.getEmail())).thenReturn(token);

        when(userRepository.findByEmail(token.getEmail())).thenReturn(user);

        Response result = userService.resetPassword(tokenResetPassword, requestDTO);

        assertNotNull(result);
    }
}