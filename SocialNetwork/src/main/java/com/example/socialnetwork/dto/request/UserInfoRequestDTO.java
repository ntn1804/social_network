package com.example.socialnetwork.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequestDTO {

    private String fullName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private String job;
    private String place;
}
