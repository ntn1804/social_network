package com.example.socialnetwork.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {

    @NotBlank(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Username is invalid")
    private String username;

    @NotBlank(message = "Password is invalid")
    private String password;
}
