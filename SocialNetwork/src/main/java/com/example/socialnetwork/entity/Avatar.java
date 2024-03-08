package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avatar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String filePath;
    private int isDeleted;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
