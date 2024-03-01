package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.FriendRequestDTO;
import com.example.socialnetwork.dto.response.FriendResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/request/{friendId}")
    public ResponseEntity<Response> sendFriendRequest(@PathVariable ("friendId") Long friendId){
        return ResponseEntity.ok(friendService.sendFriendRequest(friendId));
    }

    @PostMapping("/confirm/{friendRequestId}")
    public ResponseEntity<Response> confirmFriendRequest(@PathVariable ("friendRequestId") Long friendRequestId){
        return ResponseEntity.ok(friendService.confirmFriendRequest(friendRequestId));
    }

    @GetMapping
    public ResponseEntity<List<FriendResponseDTO>> getFriendRequest(){
        return ResponseEntity.ok(friendService.getFriendRequest());
    }

    @DeleteMapping ("/{friendId}")
    public ResponseEntity<Response> deleteFriendRequest(@PathVariable ("friendId") Long friendId){
        return ResponseEntity.ok(friendService.deleteFriend(friendId));
    }
}
