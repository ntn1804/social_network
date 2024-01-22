package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.FriendRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/friend-request/{friendId}")
    public ResponseEntity<Response> sendFriendRequest(@PathVariable ("friendId") Long friendId){
        return friendService.sendFriendRequest(friendId);
    }

    @PostMapping("/confirm-request/{friendId}")
    public ResponseEntity<Response> confirmFriendRequest(@PathVariable ("friendId") Long friendId){
        return friendService.confirmFriendRequest(friendId);
    }

    @DeleteMapping ("/delete-request/{friendId}")
    public ResponseEntity<Response> deleteFriendRequest(@PathVariable ("friendId") Long friendId){
        return friendService.deleteFriendRequest(friendId);
    }
}
