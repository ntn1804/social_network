package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.FriendResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Friend;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {
    @InjectMocks
    private FriendServiceImpl friendService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRepository friendRepository;

    @Test
    void testSendFriendRequest_ToYourself() {
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
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Optional<User> optionalFriend = Optional.of(friend);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(userRepository.findById(friend.getId())).thenReturn(optionalFriend);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.sendFriendRequest(friend.getId());
        });
    }

    @Test
    void testSendFriendRequest_AlreadyFriends() {
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
        Optional<User> optionalFriend = Optional.of(user2);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(userRepository.findById(user2.getId())).thenReturn(optionalFriend);

        Friend friend = Friend.builder()
                .requestStatus("Accepted")
                .build();
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(friend);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.sendFriendRequest(user2.getId());
        });
    }

    @Test
    void testSendFriendRequest_AlreadySent() {
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
        Optional<User> optionalFriend = Optional.of(user2);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(userRepository.findById(user2.getId())).thenReturn(optionalFriend);

        Friend friend = Friend.builder()
                .user(user)
                .requestStatus("Pending")
                .build();
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(friend);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.sendFriendRequest(user2.getId());
        });
    }

    @Test
    void testSendFriendRequest_SuccessCase1() {
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
        Optional<User> optionalFriend = Optional.of(user2);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(userRepository.findById(user2.getId())).thenReturn(optionalFriend);

        Friend friend = Friend.builder()
                .user(user2)
                .friend(user)
                .requestStatus("Pending")
                .build();
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(friend);

        Response result = friendService.sendFriendRequest(user2.getId());
        assertNotNull(result);
    }

    @Test
    void testSendFriendRequest_SuccessCase2() {
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
        Optional<User> optionalFriend = Optional.of(user2);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(userRepository.findById(user2.getId())).thenReturn(optionalFriend);
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(null);

        Response result = friendService.sendFriendRequest(user2.getId());
        assertNotNull(result);
    }

    @Test
    void testConfirmFriendRequest_AcceptByYourself() {
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

        Friend friend = Friend.builder()
                .id(1L)
                .user(user)
                .requestStatus("Pending")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Optional<Friend> optionalFriend = Optional.of(friend);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(friendRepository.findById(friend.getId())).thenReturn(optionalFriend);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.confirmFriendRequest(friend.getId());
        });
    }

    @Test
    void testConfirmFriendRequest_AlreadyFriends() {
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

        Friend friend = Friend.builder()
                .id(1L)
                .requestStatus("Accepted")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Optional<Friend> optionalFriend = Optional.of(friend);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(friendRepository.findById(friend.getId())).thenReturn(optionalFriend);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.confirmFriendRequest(friend.getId());
        });
    }

    @Test
    void testConfirmFriendRequest_SuccessCase() {
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

        Friend friend = Friend.builder()
                .id(1L)
                .user(user2)
                .requestStatus("Pending")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Optional<Friend> optionalFriend = Optional.of(friend);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(friendRepository.findById(friend.getId())).thenReturn(optionalFriend);

        Response result = friendService.confirmFriendRequest(friend.getId());
        assertNotNull(result);
    }

    @Test
    void testGetFriendRequest() {
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
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        User friend2 = User.builder()
                .id(3L)
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Optional<User> optionalFriend = Optional.of(friend);
        Optional<User> optionalFriend2 = Optional.of(friend2);

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((authentication.getPrincipal())).thenReturn(userDetails);
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        List<Long> friendIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        when(friendRepository.findFriendRequestIdsByUserId(user.getId())).thenReturn(friendIds);
        when(userRepository.findById(friend.getId())).thenReturn(optionalFriend);
        when(userRepository.findById(friend2.getId())).thenReturn(optionalFriend2);

        List<FriendResponseDTO> friendResponseDTOList = friendService.getFriendRequest();
        assertNotNull(friendResponseDTOList);
    }

    @Test
    void testDeleteFriend_Yourself() {
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

        assertThrows(ResponseStatusException.class, () -> {
            friendService.deleteFriend(user2.getId());
        });
    }

    @Test
    void testDeleteFriend_FriendNull() {
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
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            friendService.deleteFriend(user2.getId());
        });
    }

    @Test
    void testDeleteFriend_SuccessCase() {
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

        Friend friend = Friend.builder().build();
        when(friendRepository.findByUserIdAndFriendId(user.getId(), user2.getId())).thenReturn(friend);

        Response result = friendService.deleteFriend(user2.getId());
        assertNotNull(result);
    }
}