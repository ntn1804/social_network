package com.example.socialnetwork.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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
}
