package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "otp")
public class Otp extends BaseEntity{

    @Column
    private String username;

    @Column
    private String otpCode;

    @Column
    private LocalDateTime expired;

    @Column
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
