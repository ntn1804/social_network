package com.example.socialnetwork.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostResponseDTO {
    private Long postId;
    private String username;
    private String postContent;
    private List<PostImageDTO> postImageDTOList;
}
