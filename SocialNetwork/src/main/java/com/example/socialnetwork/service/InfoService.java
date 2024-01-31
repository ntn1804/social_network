package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import org.springframework.http.ResponseEntity;

public interface InfoService {
    UserInfoResponseDTO updateInfo(UserInfoRequestDTO requestDTO);

    UserInfoResponseDTO getUserInfo(Long userId);

    UserInfoResponseDTO getMyInfo();
}
