package com.example.socialnetwork.dto.request;

import com.example.socialnetwork.util.ValidPassword;
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

    @NotBlank(message = "Invalid username")
    private String username;

    @ValidPassword
    private String password;
}
