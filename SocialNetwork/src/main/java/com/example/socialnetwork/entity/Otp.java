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
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String otpCode;

    @Column
    private LocalDateTime expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
