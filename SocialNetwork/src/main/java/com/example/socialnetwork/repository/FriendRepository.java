package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserIdAndFriendId(Long id, Long friendId);

    List<Friend> findByUserId(Long id);
    List<Friend> findFriendIdById(Long id);
}
