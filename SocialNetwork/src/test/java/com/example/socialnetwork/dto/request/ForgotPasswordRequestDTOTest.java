package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordRequestDTOTest {
    @InjectMocks ForgotPasswordRequestDTO forgotPasswordRequestDTO;

    @Test
    void testGetEmail() {
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO();
        requestDTO.setEmail("test@gmail.com");

        ForgotPasswordRequestDTO requestDTO2 = ForgotPasswordRequestDTO.builder().build();
        assertNotNull(requestDTO2);

        String result = requestDTO.getEmail();
        assertEquals("test@gmail.com", result);
    }
}