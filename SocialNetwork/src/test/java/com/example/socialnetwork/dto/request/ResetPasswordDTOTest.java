package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordDTOTest {
    @InjectMocks
    private ResetPasswordDTO resetPasswordDTO;

    @Test
    void testResetPasswordDTO() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setNewPassword("test");

        String newPassword = resetPasswordDTO.getNewPassword();
        assertEquals("test", newPassword);
    }
}