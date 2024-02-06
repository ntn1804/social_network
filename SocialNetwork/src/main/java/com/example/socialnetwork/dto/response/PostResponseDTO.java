package com.example.socialnetwork.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostResponseDTO {
    private UserDTO user;
    private String text;
    private List<PostImageDTO> postImageDTOList;
}
