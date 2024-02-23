package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.UserInfoRequestDTO;
import com.example.socialnetwork.dto.response.UserInfoResponseDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfoServiceImplTest {

    @InjectMocks
    private InfoServiceImpl infoService;
    @Mock
    private UserRepository userRepository;

    @Test
    void testUpdateInfo() {
        UserInfoRequestDTO requestDTO = new UserInfoRequestDTO(
                "testFullname",
                Date.from(Instant.now()),
                "fuho",
                "vietnam"
        );

        User user = User.builder()
                .username("testUsername")
                .email("testEmail")
                .fullName(requestDTO.getFullName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .job(requestDTO.getJob())
                .place(requestDTO.getPlace())
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

        UserInfoResponseDTO result = infoService.updateInfo(requestDTO);

        assertNotNull(result);
    }

    @Test
    void testGetUserInfo() {
        Long userId = 1L;

        User user = User.builder()
                .username("testUsername")
                .email("testEmail")
                .fullName("testFullname")
                .dateOfBirth(Date.from(Instant.now()))
                .job("fuho")
                .place("vietnam")
                .role("USER")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findById(userId)).thenReturn(optionalUser);

        UserInfoResponseDTO result = infoService.getUserInfo(userId);

        assertNotNull(result);
    }

    @Test
    void testGetMyInfo() {
        User user = User.builder()
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

        UserInfoResponseDTO result = infoService.getMyInfo();

        assertNotNull(result);
    }
}