package com.example.socialnetwork.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReactResponseDTOTest {
    @InjectMocks
    private ReactResponseDTO reactResponseDTO;

    @Test
    void testReactResponseDTO() {
        ReactResponseDTO responseDTO = new ReactResponseDTO();
        responseDTO.setReact("like");

        ReactResponseDTO responseDTO2 = ReactResponseDTO.builder().build();
        assertNotNull(responseDTO2);

        String result = responseDTO.getReact();
        assertEquals("like", result);
    }
}