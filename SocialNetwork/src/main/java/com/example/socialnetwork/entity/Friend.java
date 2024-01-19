package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend extends BaseEntity {

    @Column(name = "request_status")
    private String requestStatus;

    private String isFriend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

}
