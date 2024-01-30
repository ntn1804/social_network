package com.example.testvalidate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegistrationRequestDTO {

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Username is invalid")
    private String username;

    @NotBlank(message = "Password is invalid")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
