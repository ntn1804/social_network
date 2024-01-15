package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface InfoService {
    ResponseEntity<Response> updateInfo(UserInfoRequestDTO requestDTO);
}
