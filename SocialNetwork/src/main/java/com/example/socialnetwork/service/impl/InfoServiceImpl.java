package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<Response> updateInfo(UserInfoRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> listUser = userRepository.findByUsername(userDetails.getUsername());
        if (listUser.isPresent()){
            User user = listUser.get();
            user.setDateOfBirth(requestDTO.getDateOfBirth());
            user.setJob(requestDTO.getJob());
            user.setPlace(requestDTO.getPlace());
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest().body(Response.builder()
                            .statusCode(400)
                            .responseMessage("Updated unsuccessfully")
                    .build());
        }
        return ResponseEntity.ok(Response.builder()
                        .statusCode(200)
                        .responseMessage("Updated successfully")
                        .userInfo(UserInfoResponseDTO.builder()
                                .fullName(requestDTO.getFullName())
                                .dateOfBirth(requestDTO.getDateOfBirth())
                                .job(requestDTO.getJob())
                                .place(requestDTO.getPlace())
                                .build())
                .build());
    }

    @Override
    public ResponseEntity<Response> getUserInfo(Long userId) {

        List<User> userList = userRepository.findAll();
        List<Long> userIdList = new ArrayList<>();

        for (User id : userList) {
            userIdList.add(id.getId());
        }

        if (!userIdList.contains(userId)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("User does not exist")
                    .build());
        }

        Optional<User> user = userRepository.findById(userId);

        return user.map(value -> ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .userInfo(UserInfoResponseDTO.builder()
                        .email(value.getEmail())
                        .username(value.getUsername())
                        .fullName(value.getFullName())
                        .dateOfBirth(value.getDateOfBirth())
                        .job(value.getJob())
                        .place(value.getPlace())
                        .build())
                .build())).orElse(null);
    }
}
