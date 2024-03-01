package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.TokenResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PasswordRepository extends JpaRepository<TokenResetPassword, Long> {
    TokenResetPassword findByEmail(String email);
    @Query(value = "SELECT * FROM `social-network`.token_reset_password AS token\n" +
            "WHERE token.token_series = ?1\n" +
            "AND token.email = ?2", nativeQuery = true)
    TokenResetPassword findByTokenSeriesAndEmail(String token, String email);
}
