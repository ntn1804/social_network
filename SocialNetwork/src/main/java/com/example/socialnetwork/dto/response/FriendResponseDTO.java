package com.example.socialnetwork.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendResponseDTO {
    private Long friendId;
    private String friendUsername;
}
