package com.example.socialnetwork.entity;

import com.example.socialnetwork.util.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column
    private PostStatus postStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<React> reactList;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column
    private List<PostImage> postImageList;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    private int isDeleted;

    @Override
    public int compareTo(Post o) {
        return -this.createdDate.compareTo(o.createdDate);
    }
}
