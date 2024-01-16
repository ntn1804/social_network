package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
