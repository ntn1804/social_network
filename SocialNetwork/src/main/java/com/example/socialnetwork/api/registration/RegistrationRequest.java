package com.example.socialnetwork.api.registration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private String email;
    private String username;
    private String password;
}
