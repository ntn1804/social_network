package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.React;
import com.example.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactRepo extends JpaRepository<React, Long> {
    React findByPostIdAndUser(Long postId, User user);
}
