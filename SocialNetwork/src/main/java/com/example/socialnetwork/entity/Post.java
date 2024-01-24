package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column
    private String image;

    @Column
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<React> reactList;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    @Column
    @CreatedDate
    private Date createdDate;

    @Override
    public int compareTo(Post o) {
        return getCreatedDate().compareTo(o.getCreatedDate());
    }
}
