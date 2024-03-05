package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.ReactRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.ReactService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reacts")
public class ReactController {

    @Autowired
    private ReactService reactService;

    @PostMapping("/{postId}")
    @Operation(summary = "React a post.")
    public ResponseEntity<Response> reactPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(reactService.reactPost(postId));
    }
}
