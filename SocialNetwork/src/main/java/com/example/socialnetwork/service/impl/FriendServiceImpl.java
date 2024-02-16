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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Override
    public Response sendFriendRequest(Long friendId) {
        Response response = Response.builder().build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<User> optionalUserFriend = userRepository.findById(friendId);
        User userFriend = optionalUserFriend
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // send friend request to yourself
        if (user.getId().equals(friendId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not send friend request to yourself");
        }

        Friend friendRequest = friendRepository.findByUserIdAndFriendId(user.getId(), userFriend.getId());
        if (friendRequest != null) {

            // friends already
            if (friendRequest.getRequestStatus().equals("Accepted")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are friends already");
            }

            // request status = pending
            if (friendRequest.getRequestStatus().equals("Pending")
                    && user.getId().equals(friendRequest.getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already sent friend request");
            }

            // user send friend request && friend send friend request back.
            if (friendRequest.getRequestStatus().equals("Pending")
                    && user.getId().equals(friendRequest.getFriend().getId())) {
                friendRequest.setRequestStatus("Accepted");
                friendRepository.save(friendRequest);
                response.setResponseMessage("Confirm friend request successfully");
            }
        }

        // friend = null
        if (friendRequest == null) {
            Friend newFriendRequest = Friend.builder()
                    .user(user)
                    .friend(userFriend)
                    .requestStatus("Pending")
                    .build();
            friendRepository.save(newFriendRequest);
            response.setResponseMessage("Sent friend request successfully");
        }
        return response;
    }

    @Override
    public Response confirmFriendRequest(Long friendRequestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Friend> optionalFriendRequest = friendRepository.findById(friendRequestId);
        Friend friendRequest = optionalFriendRequest
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found"));

        if (user.getId().equals(friendRequest.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not accept friend request by yourself");
        }

        if (friendRequest.getRequestStatus().equals("Accepted")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are friends already");
        }

        if (friendRequest.getRequestStatus().equals("Pending")) {
            friendRequest.setRequestStatus("Accepted");
            friendRequest.setCreatedDate(LocalDateTime.now());
            friendRepository.save(friendRequest);
        }
        return Response.builder()
                .responseMessage("Confirm friend request successfully")
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

    @Override
    public Response deleteFriend(Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        User user = optionalUser
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Friend friend = friendRepository.findByUserIdAndFriendId(user.getId(), friendId);
        if (friend == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not friends yet");
        }
        friendRepository.delete(friend);

        return Response.builder()
                .responseMessage("Deleted friend successfully")
                .build();
    }
}
