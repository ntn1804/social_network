package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.React;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactRepo extends JpaRepository<React, Long> {
    React findByReact(String react);
}
