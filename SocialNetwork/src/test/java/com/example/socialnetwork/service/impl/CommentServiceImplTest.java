package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.CommentRequestDTO;
import com.example.socialnetwork.dto.response.CommentResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Comment;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.CommentRepository;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.PostRepository;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private FriendRepository friendRepository;

    @Test
    void testComment_DeletedPost() {
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
                .isDeleted(1)
                .postStatus(PostStatus.PUBLIC)
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            commentService.comment(post.getId(), null);
        });
    }

    @Test
    void testComment_PostNotFound() {
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
                .postStatus(PostStatus.PRIVATE)
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            commentService.comment(post.getId(), null);
        });
    }

    @Test
    void testComment_NotFriends() {
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
                .user(user2)
                .isDeleted(0)
                .postStatus(PostStatus.PUBLIC)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(null);

        assertThrows(GeneralException.class, () -> {
            commentService.comment(post.getId(), null);
        });
    }

    @Test
    void testComment_SuccessCase() {
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
                .user(user2)
                .isDeleted(0)
                .postStatus(PostStatus.PUBLIC)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        Friend friend = Friend.builder()
                .build();
        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(friend);

        Response result = commentService.comment(post.getId(), new CommentRequestDTO("hihi"));
        assertNotNull(result);
    }

    @Test
    void testEditComment_DeletedPost() {
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
                .isDeleted(1)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user2)
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.editComment(comment.getId(), null);
        });
    }

    @Test
    void testEditComment_NotYourComment() {
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
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user2)
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.editComment(comment.getId(), null);
        });
    }

    @Test
    void testEditComment_BlankComment() {
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
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user)
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.editComment(comment.getId(), new CommentRequestDTO(""));
        });
    }

    @Test
    void testEditComment_SuccessCase() {
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
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user)
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        Response result = commentService.editComment(comment.getId(), new CommentRequestDTO("hihi"));
        assertNotNull(result);
    }

    @Test
    void testGetCommentPost_DeletedPost() {
        int offset = 0;
        int pageSize = 5;
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
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            commentService.getCommentPost(post.getId(), offset, pageSize);
        });
    }

    @Test
    void testGetCommentPost_NotFriends() {
        int offset = 0;
        int pageSize = 5;
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
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        when(friendRepository.findAcceptedFriendByUserIdAndFriendId(user.getId(), post.getUser().getId()))
                .thenReturn(null);

        assertThrows(GeneralException.class, () -> {
            commentService.getCommentPost(post.getId(), offset, pageSize);
        });
    }

    @Test
    void testGetCommentPost_SuccessCase() {
        int offset = 0;
        int pageSize = 5;
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
                .user(user)
                .isDeleted(0)
                .build();
        Optional<Post> optionalPost = Optional.of(post);
        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        Comment comment = Comment.builder()
                .user(user)
                .content("test")
                .build();

        Comment comment2 = Comment.builder()
                .user(user)
                .content("test")
                .build();
        List<Comment> commentList = new ArrayList<>(Arrays.asList(comment, comment2));
        when(commentRepository.findAllByPostId(post.getId())).thenReturn(commentList);

        List<CommentResponseDTO> result = commentService.getCommentPost(post.getId(), offset, pageSize);
        assertNotNull(result);
    }

    @Test
    void testDeleteComment_DeletedComment() {
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

        Comment comment = Comment.builder()
                .id(1L)
                .content("test")
                .isDeleted(1)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.deleteComment(comment.getId());
        });
    }

    @Test
    void testDeleteComment_DeletedPost() {
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
                .isDeleted(1)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user2)
                .content("test")
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.deleteComment(comment.getId());
        });
    }

    @Test
    void testDeleteComment_NotYourComment() {
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
                .isDeleted(0)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user2)
                .content("test")
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        assertThrows(GeneralException.class, () -> {
            commentService.deleteComment(comment.getId());
        });
    }

    @Test
    void testDeleteComment_SuccessCase() {
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
                .isDeleted(0)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .post(post)
                .user(user)
                .content("test")
                .isDeleted(0)
                .build();
        Optional<Comment> optionalComment = Optional.of(comment);
        when(commentRepository.findById(comment.getId())).thenReturn(optionalComment);

        Response result = commentService.deleteComment(comment.getId());
        assertNotNull(result);
    }
}