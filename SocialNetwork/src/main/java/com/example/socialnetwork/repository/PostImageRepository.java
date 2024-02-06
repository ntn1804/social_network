package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Query(value = "SELECT * FROM `social-network`.post_image\n" +
            "WHERE post_image.post_id = ?1 \n" +
            "AND post_image.is_deleted = 0;", nativeQuery = true)
    List<PostImage> findAllByPostId(Long postId);
}
