package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String role;

    @Column
    private String fullName;

    @Column
    private Date dateOfBirth;

    @Column
    private String job;

    @Column
    private String place;

    @OneToMany(mappedBy = "user")
    private List<Otp> otpList;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    @OneToMany(mappedBy = "user")
    private List<React> reactList;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user")
    private List<Friend> userList;

    @OneToMany(mappedBy = "friend")
    private List<Friend> friendList;
}
