package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.request.ReactRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.service.ReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/react")
public class ReactController {

    @Autowired
    private ReactService reactService;

    @PostMapping("/react-post/{postId}")
    public ResponseEntity<Response> reactPost(@PathVariable("postId") Long postId,
                                            @RequestBody ReactRequestDTO requestDTO) {
        return reactService.reactPost(postId, requestDTO);
    }
}
