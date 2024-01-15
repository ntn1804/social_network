package com.example.socialnetwork.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationResponseDTO {
    private String email;
    private String username;
}
