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
@Table(name = "token_reset_password")
public class TokenResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String token;

    @Column
    private LocalDateTime expired;
}
