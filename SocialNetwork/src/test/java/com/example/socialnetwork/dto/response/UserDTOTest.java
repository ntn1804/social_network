package com.example.socialnetwork.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDTOTest {
    @InjectMocks
    private UserDTO userDTO;

    @Test
    void testUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("test");

        UserDTO userDTO2 = UserDTO.builder().build();
        assertNotNull(userDTO2);

        Long id = userDTO.getId();
        String username = userDTO.getUsername();

        assertEquals(1L, id);
        assertEquals("test", username);
    }
}