package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserIdAndFriendId(Long id, Long friendId);

    @Query(value = "SELECT * FROM `social-network`.friend \n" +
            "WHERE friend.request_status = 'Accepted' \n" +
            "AND (friend.user_id = ?1 OR friend.friend_id = ?1)", nativeQuery = true)
    List<Friend> friendList(Long userId);
}
