package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.request.RegistrationRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PasswordRepository;
import com.example.socialnetwork.repository.UserRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordRepository passwordRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void registerUser() {
        //given

        //when
        when(userRepository.findByEmailOrUsername(anyString(), anyString())).thenReturn(new User());

        //then
        ResponseEntity<Response> abc = userService.registerUser(new RegistrationRequestDTO());
        ResponseEntity<Response> bca = null;
        Assertions.assertEquals(bca,abc);




    }
}