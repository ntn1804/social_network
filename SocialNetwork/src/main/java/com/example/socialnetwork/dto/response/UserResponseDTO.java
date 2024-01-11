package com.example.socialnetwork.dto.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {
    private String email;
    private String username;
    private String realName;
    private Date dateOfBirth;
    private String job;
    private String place;
}
