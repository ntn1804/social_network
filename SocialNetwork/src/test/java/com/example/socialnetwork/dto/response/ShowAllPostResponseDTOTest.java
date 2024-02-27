package com.example.socialnetwork.dto.response;

import com.example.socialnetwork.dto.request.OtpValidationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShowAllPostResponseDTOTest {
    @InjectMocks
    private ShowAllPostResponseDTO showAllPostResponseDTO;

    @Test
    void testShowAllPostResponseDTO() {
        ShowAllPostResponseDTO responseDTO = new ShowAllPostResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setText("123456");
        responseDTO.setImage("123456");
        responseDTO.setUserId(1L);

        ShowAllPostResponseDTO responseDTO2 = ShowAllPostResponseDTO.builder().build();
        assertNotNull(responseDTO2);

        Long result = responseDTO.getId();
        String result2 = responseDTO.getText();
        String result3 = responseDTO.getImage();
        Long result4 = responseDTO.getUserId();


        assertEquals(1L, result);
        assertEquals("123456", result2);
        assertEquals("123456", result3);
        assertEquals(1L, result4);
    }
}