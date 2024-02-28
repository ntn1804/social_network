package com.example.socialnetwork.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendResponseDTOTest {
    @InjectMocks
    private FriendResponseDTO friendResponseDTO;

    @Test
    void testFriendResponseDTO() {
        FriendResponseDTO responseDTO = new FriendResponseDTO();
        responseDTO.setFriendId(1L);
        responseDTO.setFriendUsername("test");

        FriendResponseDTO responseDTO2 = FriendResponseDTO.builder().build();
        assertNotNull(responseDTO2);

        Long friendId = responseDTO.getFriendId();
        String friendUsername = responseDTO.getFriendUsername();

        assertEquals(1L, friendId);
        assertEquals("test", friendUsername);
    }
}