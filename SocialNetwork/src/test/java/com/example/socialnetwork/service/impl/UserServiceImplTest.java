package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldThrowResponseStatusExceptionWhenRequestDTOHasBeenRegistered() {
        // Given
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(
                "hgjhk@gmail.com",
                "jhgkjh",
                "1234");

        User user = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);

        // When
        var exception = assertThrows(ResponseStatusException.class,
                () -> userRepository.findByEmailOrUsername(requestDTO.getEmail(), requestDTO.getUsername()));
        assertEquals("Email or Username has been registered", exception.getMessage());
    }

    @Test
    public void shouldSaveUser() {
        // Given
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(
                "test@gmail.com",
                "test",
                "1234");

        // When
        User user = User.builder()
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);

        // Then
        assertEquals(user.getEmail(), requestDTO.getEmail());
        assertEquals(user.getUsername(), requestDTO.getUsername());
        assertEquals(user.getPassword(), passwordEncoder.encode(requestDTO.getPassword()));
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
}