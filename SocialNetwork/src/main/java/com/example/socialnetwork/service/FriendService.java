package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.FriendRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface FriendService {
    ResponseEntity<Response> sendFriendRequest(Long friendId);
    ResponseEntity<Response> confirmFriendRequest(Long friendId);
    ResponseEntity<Response> deleteFriendRequest(Long friendId);
}
