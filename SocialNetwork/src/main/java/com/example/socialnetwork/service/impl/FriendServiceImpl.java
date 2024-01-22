package com.example.socialnetwork.service.impl;
import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FriendRepo friendRepo;

    @Override
    public ResponseEntity<Response> sendFriendRequest(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        if (friendId != null) {
            Optional<User> usersFriend = userRepo.findById(friendId);
            Friend existingFriendRequest = friendRepo.findByUserIdAndFriendId(user.get().getId(), friendId);
            Friend friend = friendRepo.findByUserIdAndFriendId(friendId, user.get().getId());

            if (friend != null){
                if (friend.getRequestStatus().equals("Accepted")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                } else if (friend.getRequestStatus().equals("Pending")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("They already sent you a friend request")
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
                    friendRepo.save(newFriendRequest);
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
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        if (friendId != null) {
            Friend friend = friendRepo.findByUserIdAndFriendId(friendId, user.get().getId());
            Friend friend1 = friendRepo.findByUserIdAndFriendId(user.get().getId(), friendId);

            if (friend1 != null) {
                return ResponseEntity.badRequest().body(Response.builder()
                        .statusCode(400)
                        .responseMessage("You can not confirm friend request by yourself")
                        .build());
            }

            if (friend != null) {

                if (friend.getRequestStatus().equals("Pending")) {
                    friend.setRequestStatus("Accepted");
                    friend.setIsFriend("1");
                    friendRepo.save(friend);

                } else if (friend.getRequestStatus().equals("Accepted")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                }
            } else {
                return ResponseEntity.badRequest().body(Response.builder()
                        .statusCode(400)
                        .responseMessage("You have not sent friend request yet")
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
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        if (friendId != null) {
            Friend friend = friendRepo.findByUserIdAndFriendId(friendId, user.get().getId());
            Friend friend1 = friendRepo.findByUserIdAndFriendId(friendId, user.get().getId());

            if (friend1 != null) {
                friendRepo.delete(friend1);
                return ResponseEntity.ok(Response.builder()
                        .statusCode(200)
                        .responseMessage("Delete friend request successfully")
                        .build());
            }
            if (friend != null) {
                if (friend.getRequestStatus().equals("Accept")) {
                    return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                } else if (friend.getRequestStatus().equals("Pending")) {
                    friendRepo.delete(friend);
                }
            }
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Delete friend request successfully")
                .build());
    }
}
