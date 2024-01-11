package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepo extends JpaRepository<Otp, Long> {
    Otp findByUsername(String username);
}
