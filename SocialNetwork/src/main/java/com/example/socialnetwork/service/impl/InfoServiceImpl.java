package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserInfoResponseDTO updateInfo(UserInfoRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFullName(requestDTO.getFullName());
            user.setDateOfBirth(requestDTO.getDateOfBirth());
            user.setJob(requestDTO.getJob());
            user.setPlace(requestDTO.getPlace());
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        return UserInfoResponseDTO.builder()
                .email(optionalUser.get().getEmail())
                .username(optionalUser.get().getUsername())
                .fullName(requestDTO.getFullName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .job(requestDTO.getJob())
                .place(requestDTO.getPlace())
                .build();
    }

    @Override
    public UserInfoResponseDTO getUserInfo(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserInfoResponseDTO.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .dateOfBirth(user.getDateOfBirth())
                    .job(user.getJob())
                    .place(user.getPlace())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @Override
    public UserInfoResponseDTO getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserInfoResponseDTO.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .dateOfBirth(user.getDateOfBirth())
                    .job(user.getJob())
                    .place(user.getPlace())
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lỗi này");
    }
}
