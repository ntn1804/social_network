package com.example.socialnetwork.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserInfoResponseDTOTest {
    @InjectMocks
    private UserInfoResponseDTO userInfoResponseDTO;

    @Test
    void testUserInfoResponseDTO() {
        UserInfoResponseDTO responseDTO = new UserInfoResponseDTO();
        responseDTO.setEmail("test");
        responseDTO.setUsername("test");
        responseDTO.setFullName("test");
        responseDTO.setJob("test");
        responseDTO.setPlace("test");

        UserInfoResponseDTO responseDTO2 = UserInfoResponseDTO.builder().build();
        assertNotNull(responseDTO2);

        String email = responseDTO.getEmail();
        String username = responseDTO.getEmail();
        String fullName = responseDTO.getEmail();
        String job = responseDTO.getEmail();
        String place = responseDTO.getEmail();

        assertEquals("test", email);
        assertEquals("test", username);
        assertEquals("test", fullName);
        assertEquals("test", job);
        assertEquals("test", place);
    }
}