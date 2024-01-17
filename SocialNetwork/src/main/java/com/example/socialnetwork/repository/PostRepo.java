package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByFilePath(String filePath);

    Post findByFilePathAndText(String filePath, String text);
}
