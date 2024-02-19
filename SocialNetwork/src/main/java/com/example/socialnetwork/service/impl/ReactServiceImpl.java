package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.React;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.ReactRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.ReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReactServiceImpl implements ReactService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReactRepository reactRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Override
    public Response reactPost(Long postId) {
        Response response = Response.builder().build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        User user = optionalUser
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (post.getIsDeleted() == 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is deleted");
        }

        if (post.getPrivacy().equals("only me")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        // post cua minh
        if (post.getUser().getId().equals(user.getId())) {
            createReactPost(postId, response, user, post);
        }

        // post khong phai cua minh
        if (!post.getUser().getId().equals(user.getId())) {

            // co phai ban k
            Friend friend = friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId());
            if (friend == null) {

                // post privacy
                if (post.getPrivacy().equals("public")) {
                    createReactPost(postId, response, user, post);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
                }
            }

            if (friend != null) {
                if (!post.getPrivacy().equals("only me")) {
                    createReactPost(postId, response, user, post);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
                }
            }
        }
        return response;
    }

    private void createReactPost(Long postId, Response response, User user, Post post) {
        React react = reactRepository.findByPostIdAndUser(postId, user);
        if (react != null) {
            reactRepository.delete(react);
            response.setResponseMessage("Unliked post");
        }

        if (react == null) {
            reactRepository.save(React.builder()
                    .react("Like")
                    .user(user)
                    .post(post)
                    .createdDate(LocalDateTime.now())
                    .isDeleted(0)
                    .build());
            response.setResponseMessage("Liked post");
        }
    }
}
