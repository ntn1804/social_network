package com.example.socialnetwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequestDTO {
    private String realName;
    private Date dateOfBirth;
    private String job;
    private String place;
}
