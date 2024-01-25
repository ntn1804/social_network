package com.example.socialnetwork.dto.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponseDTO {
    private String email;
    private String username;
    private String fullName;
    private Date dateOfBirth;
    private String job;
    private String place;
}
