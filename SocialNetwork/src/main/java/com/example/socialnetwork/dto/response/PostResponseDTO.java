package com.example.socialnetwork.dto.response;

import com.example.socialnetwork.util.PostStatus;
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
    private PostStatus postStatus;
    private String postContent;
    private List<PostImageDTO> postImages;
}
