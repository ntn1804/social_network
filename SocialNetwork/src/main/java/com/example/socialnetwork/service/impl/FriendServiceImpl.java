package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.FriendRequestDTO;
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
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FriendRepo friendRepo;

    @Override
    public ResponseEntity<Response> sendFriendRequest(Long friendId, FriendRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        if (friendId != null){
            Optional<User> usersFriend = userRepo.findById(friendId);

            Friend existingFriendRequest = friendRepo.findByUserIdAndFriendId(user.get().getId(), friendId);
            if (existingFriendRequest.getRequestStatus() != null){
                Friend friend = Friend.builder()
                        .user(user.orElse(null))
                        .friend(usersFriend.orElse(null))
                        .requestStatus(requestDTO.getRequestStatus())
                        .build();
                friendRepo.save(friend);
            }
        }
        return null;
    }
}
