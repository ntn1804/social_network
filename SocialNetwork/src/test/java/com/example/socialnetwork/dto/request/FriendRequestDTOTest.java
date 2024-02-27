package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendRequestDTOTest {
    @InjectMocks
    private FriendRequestDTO friendRequestDTO;

    @Test
    void testFriendRequestDTO() {
        FriendRequestDTO requestDTO = new FriendRequestDTO();
        requestDTO.setRespondFriendRequest("Pending");

        String result = requestDTO.getRespondFriendRequest();
        assertEquals("Pending", result);
    }
}