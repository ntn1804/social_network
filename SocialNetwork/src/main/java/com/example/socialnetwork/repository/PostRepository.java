package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM `social-network`.post\n" +
            "WHERE post.is_deleted = 0\n" +
            "AND post.user_id = ?1", nativeQuery = true)
    List<Post> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM `social-network`.post\n" +
            "WHERE (post.privacy = 'public' OR post.privacy = 'friends')\n" +
            "AND post.is_deleted = 0\n" +
            "AND post.user_id = ?1", nativeQuery = true)
    List<Post> findAllByFriendId(Long userId);

}
