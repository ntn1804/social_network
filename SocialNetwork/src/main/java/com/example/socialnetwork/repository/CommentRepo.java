package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
