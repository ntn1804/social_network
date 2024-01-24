package com.example.socialnetwork.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShowAllPostResponseDTO {

    private Long id;
    private String text;
    private String image;
    private Long userId;

}
