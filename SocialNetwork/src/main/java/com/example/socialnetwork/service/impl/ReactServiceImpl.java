package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.ReactRequestDTO;
import com.example.socialnetwork.dto.response.ReactResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.React;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.ReactRepository;
import com.example.socialnetwork.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReactRepository reactRepository;

    @Override
    public ResponseEntity<Response> reactPost(Long postId, ReactRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if(postId != null){
            Optional<Post> post = postRepository.findById(postId);
            React existingReact = reactRepository.findByPostIdAndUser
                                            (postId, user.orElse(null));
            if (existingReact != null && existingReact.getReact().equals(requestDTO.getReact())){
                reactRepository.delete(existingReact);
                return ResponseEntity.ok(Response.builder()
                        .statusCode(200)
                        .responseMessage("OK")
                        .reactResponse(ReactResponseDTO.builder()
                                .react("remove react")
                                .build())
                        .build());
            } else if (existingReact != null){
                existingReact.setReact(requestDTO.getReact());
                reactRepository.save(existingReact);
            } else {
                React newReact = React.builder()
                        .user(user.orElse(null))
                        .post(post.orElse(null))
                        .react(requestDTO.getReact())
                        .build();
                reactRepository.save(newReact);
            }
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
