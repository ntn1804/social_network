package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Avatar, Long> {

    @Query(value = "SELECT * FROM `social-network`.avatar \n" +
            "WHERE id = (SELECT max(id) FROM `social-network`.avatar \n" +
            "WHERE avatar.is_deleted = 0 AND avatar.user_id = ?1);", nativeQuery = true)
    Avatar findByUserId(Long userId);
}
