package com.example.socialnetwork.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentRequestDTOTest {
    @InjectMocks
    CommentRequestDTO commentRequestDTO;

    @Test
    void getContent() {
        commentRequestDTO.setContent("abc");

        String result = commentRequestDTO.getContent();
        assertEquals("abc",result);

    }
}