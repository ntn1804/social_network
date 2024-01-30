package com.example.testvalidation.repository;

import com.example.testvalidation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(String email, String username);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
