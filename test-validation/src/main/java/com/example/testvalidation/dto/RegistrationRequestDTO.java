package com.example.testvalidation.dto;

import jakarta.validation.constraints.Email;
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

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Username is invalid")
    private String username;

    @NotBlank(message = "Password is invalid")
    private String password;
}
