package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Override
    public ResponseEntity<Response> sendFriendRequest(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<User> userList = userRepository.findAll();
        List<Long> userIdList = new ArrayList<>();

        for (User id : userList) {
            userIdList.add(id.getId());
        }

        if (!userIdList.contains(friendId)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("User does not exist")
                    .build());
        }

        if (friendId != null) {
            Optional<User> usersFriend = userRepository.findById(friendId);
            Friend existingFriendRequest = friendRepository.findByUserIdAndFriendId(user.get().getId(), friendId);
            Friend friend = friendRepository.findByUserIdAndFriendId(friendId, user.get().getId());

            if (friend != null) {
                if (friend.getRequestStatus().equals("Accepted")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                } else if (friend.getRequestStatus().equals("Pending")) {
                    friend.setRequestStatus("Accepted");
                    friendRepository.save(friend);
                    return ResponseEntity.ok(Response.builder()
                            .statusCode(200)
                            .responseMessage("Confirm friend request successfully")
                            .build());
                }
            }

            if (existingFriendRequest != null) {
                return ResponseEntity.badRequest().body(Response.builder()
                        .statusCode(400)
                        .responseMessage("You already sent a friend request")
                        .build());
            } else {
                if (Objects.equals(user.get().getId(), friendId)) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("Can not send friend request to yourself")
                            .build());
                } else {
                    Friend newFriendRequest = Friend.builder()
                            .user(user.orElse(null))
                            .friend(usersFriend.orElse(null))
                            .requestStatus("Pending")
                            .build();
                    friendRepository.save(newFriendRequest);
                }
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Sent friend request successfully")
                .build());
    }

    @Override
    public ResponseEntity<Response> confirmFriendRequest(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (friendId != null) {
            Friend friend = friendRepository.findByUserIdAndFriendId(friendId, user.get().getId());
            Friend friend1 = friendRepository.findByUserIdAndFriendId(user.get().getId(), friendId);

            if (friend1 != null) {
                return ResponseEntity.badRequest().body(Response.builder()
                        .statusCode(400)
                        .responseMessage("Can not confirm friend request by yourself")
                        .build());
            }

            if (friend != null) {
                if (friend.getRequestStatus().equals("Pending")) {
                    friend.setRequestStatus("Accepted");
                    friendRepository.save(friend);

                } else if (friend.getRequestStatus().equals("Accepted")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                }
            } else {
                return ResponseEntity.badRequest().body(Response.builder()
                        .statusCode(400)
                        .responseMessage("You have not sent friend request")
                        .build());
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Confirm friend request successfully")
                .build());
    }

    @Override
    public ResponseEntity<Response> deleteFriendRequest(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (friendId != null) {
            Friend friend = friendRepository.findByUserIdAndFriendId(friendId, user.get().getId());
            Friend friend1 = friendRepository.findByUserIdAndFriendId(user.get().getId(), friendId);

            if (friend1 != null) {
                friendRepository.delete(friend1);
                return ResponseEntity.ok(Response.builder()
                        .statusCode(200)
                        .responseMessage("Delete friend request successfully")
                        .build());
            }
            if (friend != null) {
                if (friend.getRequestStatus().equals("Accept")) {
                    friendRepository.delete(friend);
                    return ResponseEntity.ok(Response.builder()
                            .statusCode(200)
                            .responseMessage("Delete friend successfully")
                            .build());
                } else if (friend.getRequestStatus().equals("Pending")) {
                    friendRepository.delete(friend);
                }
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Delete friend request successfully")
                .build());
    }
}
