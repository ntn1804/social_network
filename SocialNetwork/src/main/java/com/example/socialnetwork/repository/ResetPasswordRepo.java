package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.TokenResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepo extends JpaRepository<TokenResetPassword, Long> {
    TokenResetPassword findByEmail(String email);
    TokenResetPassword findByToken(String token);
}
