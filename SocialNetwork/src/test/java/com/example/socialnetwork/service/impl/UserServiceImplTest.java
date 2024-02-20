package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.ForgotPasswordRequestDTO;
import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.response.ForgotPasswordResponseDTO;
import com.example.socialnetwork.dto.response.RegistrationResponseDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PasswordRepository;
import com.example.socialnetwork.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserServiceImpl userServiceImpl;
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
    public void testRegisterUser_ExistingUser() {
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
    public void testRegisterUser_UniqueUser() {
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
    public void shouldSaveUserAndReturnSavedUser() {
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
    public void testForgotPassword_InvalidEmail() {
        // Given
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO(
                "test@gmail.com");

        when(userRepository.findByEmail(requestDTO.getEmail()))
                .thenReturn(null);

        // When
        assertThrows(ResponseStatusException.class, () -> {
            userService.forgotPassword(requestDTO);
        });
    }

    @Test
    public void testForgotPassword_ValidEmail() {
        // Given
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO(
                "test@gmail.com");

        User existingUser = new User();
        existingUser.setEmail(requestDTO.getEmail());

        // When
        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(existingUser);
        when(userService.generateToken(requestDTO)).thenReturn("token123");

        ForgotPasswordResponseDTO responseDTO = userService.forgotPassword(requestDTO);

        // Then
        assertEquals(responseDTO.getUrlAndTokenResetPassword(),
                responseDTO.getUrlAndTokenResetPassword());
    }

    @Test
    public void testGenerateToken_NonExistingToken() {
        // Given
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("test@gmail.com");

        // When
        when(passwordRepository.findByEmail(requestDTO.getEmail())).thenReturn(null);
        String token = userService.generateToken(requestDTO);

        // Then
        assertNotNull(token);
    }
}