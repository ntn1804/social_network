package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.ReactRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.ReactResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.React;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PostRepo;
import com.example.socialnetwork.repository.ReactRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.ReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactServiceImpl implements ReactService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ReactRepo reactRepo;

    @Override
    public ResponseEntity<Response> reactPost(Long postId, ReactRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

//        React react = reactRepo.findByReact(requestDTO.getReact());
//        if (react != null){
//
//        }

        if(postId != null){
            Optional<Post> post = postRepo.findById(postId);
            React react = React.builder()
                    .user(user.orElse(null))
                    .post(post.orElse(null))
                    .react(requestDTO.getReact())
                    .build();
            reactRepo.save(react);
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("OK")
                .reactResponse(ReactResponseDTO.builder()
                        .react(requestDTO != null ? requestDTO.getReact() : null)
                        .build())
                .build());
    }


}
