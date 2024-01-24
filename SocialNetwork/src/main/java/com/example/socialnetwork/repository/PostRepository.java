package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByFilePath(String filePath);

    Post findByFilePathAndText(String filePath, String text);

    Post findAllById(Long friendId);

    List<Post> findAllByUserId(Long userId);

    List<Post> findAllByUserIdIn(List<Long> userIdList);

}
