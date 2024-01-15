package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public ResponseEntity<Response> updateInfo(UserInfoRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> listUser = userRepo.findByUsername(userDetails.getUsername());
        if (listUser.isPresent()){
            User user = listUser.get();
            user.setRealName(requestDTO.getRealName());
            user.setDateOfBirth(requestDTO.getDateOfBirth());
            user.setJob(requestDTO.getJob());
            user.setPlace(requestDTO.getPlace());
            userRepo.save(user);
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
                                .realName(requestDTO.getRealName())
                                .dateOfBirth(requestDTO.getDateOfBirth())
                                .job(requestDTO.getJob())
                                .place(requestDTO.getPlace())
                                .build())
                .build());
    }
}
