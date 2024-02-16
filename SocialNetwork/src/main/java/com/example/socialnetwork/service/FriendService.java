package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.FriendRequestDTO;
import com.example.socialnetwork.dto.response.FriendResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendService {
    Response sendFriendRequest(Long friendId);
    Response confirmFriendRequest(Long friendRequestId);
    Response deleteFriend(Long friendId);
    List<FriendResponseDTO> getFriendRequest();
}
