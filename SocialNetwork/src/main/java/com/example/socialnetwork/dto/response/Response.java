package com.example.socialnetwork.dto.response;

import com.example.socialnetwork.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String responseMessage;
    private UserInfoResponseDTO userInfo;
    private RegistrationResponseDTO registrationResponse;
    private CommentResponseDTO commentResponse;
    private ReactResponseDTO reactResponse;
    private List<Post> timelinePost;
}
