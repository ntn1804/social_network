package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OtpValidationRequestTest {
    @InjectMocks
    private OtpValidationRequest otpValidationRequest;

    @Test
    void testOtpValidationRequest() {
        OtpValidationRequest requestDTO = new OtpValidationRequest();
        requestDTO.setUsername("Pending");
        requestDTO.setOtpCode("123456");

        String result = requestDTO.getUsername();
        String result2 = requestDTO.getOtpCode();
        assertEquals("Pending", result);
        assertEquals("123456", result2);
    }
}