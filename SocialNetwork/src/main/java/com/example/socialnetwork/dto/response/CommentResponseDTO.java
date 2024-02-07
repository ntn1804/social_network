package com.example.socialnetwork.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponseDTO {
    private UserDTO user;
    private String content;
}
