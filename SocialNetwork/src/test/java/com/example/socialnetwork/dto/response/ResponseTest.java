package com.example.socialnetwork.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResponseTest {
    @InjectMocks
    private Response response;

    @Test
    void testResponse() {
        Response response = new Response();
        response.setResponseMessage("test");
        response.setRegistrationResponse(RegistrationResponseDTO.builder()
                .username("test")
                .email("test")
                .build());
        response.setCommentResponse(CommentResponseDTO.builder()
                .content("test")
                .build());
        response.setReactResponse(new ReactResponseDTO("test"));

        Response response2 = Response.builder().build();
        assertNotNull(response2);

        String responseMessage = response.getResponseMessage();
        RegistrationResponseDTO registrationResponseDTO = response.getRegistrationResponse();
        CommentResponseDTO commentResponseDTO = response.getCommentResponse();
        ReactResponseDTO reactResponseDTO = response.getReactResponse();

        assertEquals("test", responseMessage);
        assertNotNull(registrationResponseDTO);
        assertNotNull(commentResponseDTO);
        assertNotNull(reactResponseDTO);
    }
}