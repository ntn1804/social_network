package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post extends BaseEntity{

    @Column
    private String text;

    @Column
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<React> reactList;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;
}
