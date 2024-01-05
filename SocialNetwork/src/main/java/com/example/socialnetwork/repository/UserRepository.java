package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(String email, String username);
}
