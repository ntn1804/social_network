package com.example.socialnetwork.dto.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponseDTO {
    private String realName;
    private Date dateOfBirth;
    private String job;
    private String place;
}
