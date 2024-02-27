package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.entity.*;
import com.example.socialnetwork.repository.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @InjectMocks
    private ReportServiceImpl reportService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private ReactRepository reactRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void testCountPost() {
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
                .createdDate(LocalDateTime.now().minusDays(1))
                .build();
        Post post2 = Post.builder()
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();
        List<Post> postList = new ArrayList<>(Arrays.asList(post, post2));
        when(postRepository.findAllByUserId(user.getId())).thenReturn(postList);

        Long result = reportService.countPostByCreatedDate();
        assertNotNull(result);
    }

    @Test
    void testCountFriend() {
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

        Friend friend = Friend.builder()
                .createdDate(LocalDateTime.now().minusDays(1))
                .build();
        Friend friend2 = Friend.builder()
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();
        List<Friend> friendList = new ArrayList<>(Arrays.asList(friend, friend2));
        when(friendRepository.friendList(user.getId())).thenReturn(friendList);

        Long result = reportService.countFriendByCreatedDate();
        assertNotNull(result);
    }

    @Test
    void testCountReact() {
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

        React react = React.builder()
                .createdDate(LocalDateTime.now().minusDays(1))
                .build();
        React react2 = React.builder()
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();
        List<React> recentReacts = new ArrayList<>(Arrays.asList(react, react2));
        when(reactRepository.findAllByUserId(user.getId())).thenReturn(recentReacts);

        Long result = reportService.countReactByCreatedDate();
        assertNotNull(result);
    }

    @Test
    void testCountComment() {
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
                .createdDate(LocalDateTime.now().minusDays(1))
                .build();
        Comment comment2 = Comment.builder()
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();
        List<Comment> friendList = new ArrayList<>(Arrays.asList(comment, comment2));
        when(commentRepository.findAllByUserId(user.getId())).thenReturn(friendList);

        Long result = reportService.countCommentByCreatedDate();
        assertNotNull(result);
    }

    @Test
    void testGenerateExcel() throws Exception {
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

        // Create a mock HttpServletResponse
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);

        // Mock the necessary method calls
        when(response.getOutputStream()).thenReturn(outputStream);

        // Create an instance of the class under test
//        ExcelGenerator excelGenerator = new ExcelGenerator();

        // Call the method under test
        reportService.generateExcel(response);

        // Verify that the necessary methods were called
        verify(response).setContentType("application/octet-stream");
        verify(response).setHeader("Content-Disposition", "attachment;filename=report.xls");
        verify(outputStream).close();
    }
}