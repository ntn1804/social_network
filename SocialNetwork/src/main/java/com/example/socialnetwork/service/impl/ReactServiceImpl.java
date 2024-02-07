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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<Response> reactPost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<Post> postList = postRepository.findAll();
        List<Long> postIds = new ArrayList<>();

        for (Post post : postList) {
            postIds.add(post.getId());
        }

        if (!postIds.contains(postId)) {
            return ResponseEntity.badRequest().body(Response.builder()
//                    .statusCode(400)
                    .responseMessage("Post does not exist")
                    .build());
        }
                        
        if (postId != null) {
            Optional<Post> post = postRepository.findById(postId);
            React existingReact = reactRepository.findByPostIdAndUser
                    (postId, user.orElse(null));
            if (existingReact != null) {
                reactRepository.delete(existingReact);
                return ResponseEntity.ok(Response.builder()
//                        .statusCode(200)
                        .responseMessage("Unliked post")
                        .build());
            } else {
                reactRepository.save(React.builder()
                        .react("Like")
                        .user(user.orElse(null))
                        .post(post.orElse(null))
                        .createdDate(LocalDateTime.now())
                        .isDeleted(0)
                        .build());
                return ResponseEntity.ok(Response.builder()
//                        .statusCode(200)
                        .responseMessage("Liked post")
                        .build());
            }
        }
        return null;
    }
}
