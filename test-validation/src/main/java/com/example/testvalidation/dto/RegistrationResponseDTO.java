package com.example.testvalidation.dto;

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
