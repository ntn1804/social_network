package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query(value = "SELECT * FROM `social-network`.friend\n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1)\n" +
            "AND (friend.friend_id = ?2 OR friend.user_id = ?2)", nativeQuery = true)
    Friend findByUserIdAndFriendId(Long id, Long friendId);

    @Query(value = "SELECT * FROM `social-network`.friend \n" +
            "WHERE friend.request_status = 'Accepted' \n" +
            "AND (friend.user_id = ?1 OR friend.friend_id = ?1)", nativeQuery = true)
    List<Friend> friendList(Long userId);

    @Query(value = "SELECT friend_id FROM `social-network`.friend\n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1)\n" +
            "AND friend.request_status = 'Accepted'\n" +
            "UNION\n" +
            "SELECT user_id FROM `social-network`.friend\n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1)\n" +
            "AND friend.request_status = 'Accepted'", nativeQuery = true)
    List<Long> findFriendIdsByUserId(Long userId);

    @Query(value = "SELECT * FROM `social-network`.friend \n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1) \n" +
            "AND (friend.friend_id = ?2 OR friend.user_id = ?2) \n" +
            "AND friend.request_status = 'Accepted'", nativeQuery = true)
    Friend findAcceptedFriendByUserIdAndFriendId(Long userId, Long friendId);

    @Query(value = "SELECT friend_id FROM `social-network`.friend\n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1) \n" +
            "AND friend.request_status = 'Pending'\n" +
            "UNION\n" +
            "SELECT user_id FROM `social-network`.friend\n" +
            "WHERE (friend.friend_id = ?1 OR friend.user_id = ?1) \n" +
            "AND friend.request_status = 'Pending'", nativeQuery = true)
    List<Long> findFriendRequestIdsByUserId(Long userId);


}
