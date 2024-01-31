package com.example.socialnetwork.dto.request;

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
public class ForgotPasswordRequestDTO {

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Invalid username")
    private String username;
}
