package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.React;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.ReactRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.util.PostStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactServiceImplTest {
    @InjectMocks
    private ReactServiceImpl reactService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ReactRepository reactRepository;
    @Mock
    private FriendRepository friendRepository;

    @Test
    void testReactPost_DeletedPost() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .isDeleted(1)
                .postStatus(PostStatus.PUBLIC)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            reactService.reactPost(post.getId());
        });
    }

    @Test
    void testReactPost_SuccessCase1_ReactNotNull() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .isDeleted(0)
                .user(user)
                .postStatus(PostStatus.PRIVATE)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        React react = React.builder().build();
        when(reactRepository.findByPostIdAndUser(post.getId(), user)).thenReturn(react);

        Response result = reactService.reactPost(post.getId());
        assertNotNull(result);
    }

    @Test
    void testReactPost_SuccessCase1_ReactNull() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .isDeleted(0)
                .user(user)
                .postStatus(PostStatus.PRIVATE)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(reactRepository.findByPostIdAndUser(post.getId(), user)).thenReturn(null);

        Response result = reactService.reactPost(post.getId());
        assertNotNull(result);
    }

    @Test
    void testReactPost_PrivacyPost() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .user(User.builder().id(2L).build())
                .isDeleted(0)
                .postStatus(PostStatus.PRIVATE)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            reactService.reactPost(post.getId());
        });
    }

    @Test
    void testReactPost_FriendNull() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .user(user2)
                .isDeleted(0)
                .postStatus(PostStatus.FRIENDS)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(null);

        assertThrows(GeneralException.class, () -> {
            reactService.reactPost(post.getId());
        });
    }

    @Test
    void testReactPost_FriendNull_SuccessCase() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .user(user2)
                .isDeleted(0)
                .postStatus(PostStatus.PUBLIC)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(null);

        Response result = reactService.reactPost(post.getId());
        assertNotNull(result);
    }

    @Test
    void testReactPost_FriendNotNull() {
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        Post post = Post.builder()
                .id(1L)
                .user(user2)
                .isDeleted(0)
                .postStatus(PostStatus.FRIENDS)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        Friend friend = Friend.builder().build();
        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(friend);

        Response result = reactService.reactPost(post.getId());
        assertNotNull(result);
    }
}