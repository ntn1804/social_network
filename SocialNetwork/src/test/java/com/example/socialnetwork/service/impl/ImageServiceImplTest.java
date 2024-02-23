package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.Avatar;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.ImageRepository;
import com.example.socialnetwork.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    ImageServiceImpl imageService;
    @Mock
    ImageRepository imageRepository;
    @Mock
    UserRepository userRepository;

    @Test
    void testUploadAvatar_NullContentType() {
        MultipartFile file = new MockMultipartFile(
                "fileName",
                (String) null,
                null,
                (byte[]) null
        );

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

        assertThrows(ResponseStatusException.class, () -> {
            imageService.uploadAvatar(file);
        });
    }

    @Test
    void testUploadAvatar_MediaTypeNotEqualImage() {
        MultipartFile file = new MockMultipartFile(
                "fileName",
                (String) null,
                "text/plain",
                (byte[]) null
        );

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

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        assertThrows(InvalidMediaTypeException.class, () -> {
            imageService.uploadAvatar(file);
        });
    }

    @Test
    void testUploadAvatar_SuccessfulCase() throws IOException {
        MultipartFile file = new MockMultipartFile(
                "fileName",
                (String) null,
                "image/jpeg",
                (byte[]) null
        );

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

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when((authentication.getPrincipal())).thenReturn(userDetails);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);

        UUID uuidTest = UUID.fromString("df7dcce6-3495-49e2-aa01-c649647865d9");

        try (MockedStatic<UUID> utilities = Mockito.mockStatic(UUID.class)) {
            // xu ly random cua UUID
            utilities.when(UUID::randomUUID).thenReturn(uuidTest);
        }

        Response result = imageService.uploadAvatar(file);

        assertNotNull(result);
    }

    @Test
    void testShowMyAvatar_SuccessfulCase() throws IOException {
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

        Avatar avatar = Avatar.builder()
                .filePath("C:\\Users\\nguyentrungnghia\\Pictures\\Wallpaper\\42ba7e75d3fea5f706cd1a27270fc058bd18c270a1f4fdb200d4b7d1e2e49df6")
                .build();

        when(imageRepository.findByUserId(user.getId())).thenReturn(avatar);

        ResponseEntity<?> result = imageService.showMyAvatar();

        assertNotNull(result);
    }

    @Test
    void testShowMyAvatar_NullAvatar() throws IOException {
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

        when(imageRepository.findByUserId(user.getId())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            imageService.showMyAvatar();
        });
    }
}