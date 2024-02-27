package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReactRequestDTOTest {
    @InjectMocks
    private ReactRequestDTO reactRequestDTO;

    @Test
    void testReactRequestDTO() {
        ReactRequestDTO requestDTO = new ReactRequestDTO();
        requestDTO.setReact("like");

        String result = requestDTO.getReact();
        assertEquals("like", result);
    }
}