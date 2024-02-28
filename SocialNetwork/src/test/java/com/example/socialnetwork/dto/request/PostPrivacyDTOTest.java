package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostPrivacyDTOTest {
    @InjectMocks
    private PostPrivacyDTO postPrivacyDTO;

    @Test
    void testPostPrivacyDTO() {
        PostPrivacyDTO postPrivacyDTO = new PostPrivacyDTO();
        postPrivacyDTO.setPrivacy("test");

        String privacy = postPrivacyDTO.getPrivacy();
        assertEquals("test", privacy);
    }
}