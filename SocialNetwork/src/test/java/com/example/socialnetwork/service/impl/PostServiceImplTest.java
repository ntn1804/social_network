package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.PostImage;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @InjectMocks
    private PostServiceImpl postService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private PostImageRepository postImageRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReactRepository reactRepository;

    @Test
    void testGetAllPosts() {

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("test username")
                .role("USER")
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Post post = Post.builder()
                .id(1L)
                .user(user2)
                .createdDate(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .user(user2)
                .createdDate(LocalDateTime.now().minusMinutes(30))
                .build();

        List<Long> friendIds = new ArrayList<>(Arrays.asList(1L, 2L));

        when(friendRepository.findFriendIdsByUserId(user.getId())).thenReturn(friendIds);

        List<Post> friendPostList = new ArrayList<>();
        friendPostList.add(post);
        friendPostList.add(post2);

        when(postRepository.findAllByFriendId(any())).thenReturn(friendPostList);

        PostImage postImage = PostImage.builder()
                .build();

        PostImage postImage2 = PostImage.builder()
                .build();

        List<PostImage> postImageList = new ArrayList<>(Arrays.asList(postImage, postImage2));

        when(postImageRepository.findAllByPostId(any())).thenReturn(postImageList);

        List<PostResponseDTO> result = postService.getAllPosts();

        assertNotNull(result);
    }

    @Test
    void testGetPostById_DeletedPost() {
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

        User friend = User.builder()
                .id(1L)
                .username("friendName")
                .email("friendEmail")
                .fullName("friendFullname")
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
                .user(friend)
                .isDeleted(1)
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(ResponseStatusException.class, () -> {
            postService.getPostById(post.getId());
        });
    }

    @Test
    void testGetPostById_NotFriends_PostNotPublic() {
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

        User friend = User.builder()
                .id(2L)
                .username("friendName")
                .email("friendEmail")
                .fullName("friendFullname")
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
                .user(friend)
                .isDeleted(0)
                .privacy("friends")
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), friend.getId()))
                .thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            postService.getPostById(post.getId());
        });

    }

    @Test
    void testGetPostById_Friends_OnlyMePost() {
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

        User friend = User.builder()
                .id(2L)
                .username("friendName")
                .email("friendEmail")
                .fullName("friendFullname")
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
                .user(friend)
                .isDeleted(0)
                .privacy("only me")
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), friend.getId()))
                .thenReturn(Friend.builder().build());

        assertThrows(ResponseStatusException.class, () -> {
            postService.getPostById(post.getId());
        });
    }

    @Test
    void testGetPostById_Friends_SuccessfulCase() {
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

        User friend = User.builder()
                .id(2L)
                .username("friendName")
                .email("friendEmail")
                .fullName("friendFullname")
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
                .user(friend)
                .isDeleted(0)
                .privacy("public")
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), friend.getId()))
                .thenReturn(Friend.builder().build());

        PostImage postImage = PostImage.builder()
                .filePath("C:\\Users\\nguyentrungnghia\\Pictures\\Wallpaper\\1b13991786eaaac28e118871a0d097ca77fcc8a020aa2681d60a65dd35845f97")
                .build();

        PostImage postImage2 = PostImage.builder()
                .filePath("C:\\Users\\nguyentrungnghia\\Pictures\\Wallpaper\\1bb46721ccad088c232c066efcb105c9eb4e9ae49d89b0f7436d86ab61df1aa0")
                .build();

        List<PostImage> postImageList = new ArrayList<>(Arrays.asList(postImage, postImage2));

        when(postImageRepository.findAllByPostId(post.getId())).thenReturn(postImageList);

        PostResponseDTO result = postService.getPostById(post.getId());

        assertNotNull(result);
    }

    @Test
    void testDeletePost_NotYourPost() {
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

        User friend = User.builder()
                .id(2L)
                .username("friendName")
                .email("friendEmail")
                .fullName("friendFullname")
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
                .user(friend)
                .isDeleted(0)
                .privacy("public")
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(ResponseStatusException.class, () -> {
            postService.deletePost(post.getId());
        });
    }
}