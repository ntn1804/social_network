package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepo extends JpaRepository<Friend, Long> {
    Friend findByUserIdAndFriendId(Long id, Long friendId);
}
