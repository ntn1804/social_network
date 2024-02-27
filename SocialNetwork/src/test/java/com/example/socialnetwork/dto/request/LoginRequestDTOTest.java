package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginRequestDTOTest {
    @InjectMocks
    private LoginRequestDTO loginRequestDTO;

    @Test
    void testLoginRequestDTO() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setUsername("Pending");
        requestDTO.setPassword("Accepted");

        LoginRequestDTO requestDTO2 = LoginRequestDTO.builder().build();
        assertNotNull(requestDTO2);

        String result = requestDTO.getUsername();
        String result2 = requestDTO.getPassword();
        assertEquals("Pending", result);
        assertEquals("Accepted", result2);
    }
}