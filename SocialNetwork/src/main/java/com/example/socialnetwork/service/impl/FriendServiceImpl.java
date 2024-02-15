package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.FriendResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
//                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                } else if (friend.getRequestStatus().equals("Pending")) {
                    friend.setRequestStatus("Accepted");
                    friendRepository.save(friend);
                    return ResponseEntity.ok(Response.builder()
//                            .statusCode(200)
                            .responseMessage("Confirm friend request successfully")
                            .build());
                }
            }

            if (existingFriendRequest != null) {
                return ResponseEntity.badRequest().body(Response.builder()
//                        .statusCode(400)
                        .responseMessage("You already sent a friend request")
                        .build());
            } else {
                if (Objects.equals(user.get().getId(), friendId)) {
                    return ResponseEntity.badRequest().body(Response.builder()
//                            .statusCode(400)
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
//                .statusCode(200)
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
//                        .statusCode(400)
                        .responseMessage("Can not confirm friend request by yourself")
                        .build());
            }

            if (friend != null) {
                if (friend.getRequestStatus().equals("Pending")) {
                    friend.setRequestStatus("Accepted");
                    friend.setCreatedDate(LocalDateTime.now());
                    friendRepository.save(friend);

                } else if (friend.getRequestStatus().equals("Accepted")) {
                    return ResponseEntity.badRequest().body(Response.builder()
//                            .statusCode(400)
                            .responseMessage("You are already friends")
                            .build());
                }
            } else {
                return ResponseEntity.badRequest().body(Response.builder()
//                        .statusCode(400)
                        .responseMessage("You have not sent friend request")
                        .build());
            }
        }
        return ResponseEntity.ok(Response.builder()
//                .statusCode(200)
                .responseMessage("Confirm friend request successfully")
                .build());
    }

    @Override
    public Response deleteFriend(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Friend friend = friendRepository.findByUserIdAndFriendId(user.getId(),friendId);
        if (friend == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not friends yet");
        }
        friendRepository.delete(friend);

        return Response.builder()
                .responseMessage("Deleted friend successfully")
                .build();
    }

    @Override
    public List<FriendResponseDTO> getFriendRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Long> friendIds = friendRepository.findFriendRequestIdsByUserId(user.getId());
        friendIds.remove(user.getId());

        List<FriendResponseDTO> friendResponseDTOList = new ArrayList<>();
        for (Long friendId : friendIds) {
            Optional<User> friend = userRepository.findById(friendId);
            FriendResponseDTO friendResponseDTO = FriendResponseDTO.builder()
                    .friendId(friendId)
                    .friendUsername(friend.get().getUsername())
                    .build();
            friendResponseDTOList.add(friendResponseDTO);
        }
        return friendResponseDTOList;
    }
}
