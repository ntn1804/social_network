package com.example.socialnetwork.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateOfBirth;
    private String job;
    private String place;
}
