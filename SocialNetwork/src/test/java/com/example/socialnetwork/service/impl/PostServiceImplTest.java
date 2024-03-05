package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostPrivacyDTO;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.PostResponseDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.*;
import com.example.socialnetwork.exception.GeneralException;
import com.example.socialnetwork.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void testCreatePost_EmptyPost() {
        MultipartFile[] files = null;
        PostRequestDTO requestDTO = null;

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertThrows(GeneralException.class, () -> {
            postService.createPost(files, requestDTO);
        });
    }

    @Test
    void testCreatePost_NotImageFiles() {
        MultipartFile[] files = new MockMultipartFile[]{new MockMultipartFile(
                "fileName",
                (String) null,
                null,
                (byte[]) null
        )};

        PostRequestDTO requestDTO = new PostRequestDTO("test content");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertThrows(GeneralException.class, () -> {
            postService.createPost(files, requestDTO);
        });
    }

    @Test
    void testCreatePost_MediaTypeNotEqualImage() {
        MultipartFile[] files = new MockMultipartFile[]{new MockMultipartFile(
                "fileName",
                (String) null,
                "text/plain",
                (byte[]) null
        )};

        PostRequestDTO requestDTO = new PostRequestDTO("test content");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertThrows(InvalidMediaTypeException.class, () -> {
            postService.createPost(files, requestDTO);
        });
    }

    @Test
    void testCreatePost_FilesNull_RequestDTOEmpty() {
        MultipartFile[] files = null;
        PostRequestDTO requestDTO = new PostRequestDTO("");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertThrows(GeneralException.class, () -> {
            postService.createPost(files, requestDTO);
        });
    }

    @Test
    void testCreatePost_FilesNotNull_RequestDtoNotNull() {
        MultipartFile[] files = new MockMultipartFile[]{new MockMultipartFile(
                "fileName",
                (String) null,
                "image/jpeg",
                (byte[]) null
        )};

        PostRequestDTO requestDTO = new PostRequestDTO("test content");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
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
                .user(user)
                .text(requestDTO.getText())
                .privacy("public")
                .isDeleted(0)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Response result = postService.createPost(files, requestDTO);
        assertNotNull(result);
    }

    @Test
    void testCreatePost_FilesNotNull_RequestDtoNull() {
        MultipartFile[] files = new MockMultipartFile[]{new MockMultipartFile(
                "fileName",
                (String) null,
                "image/jpeg",
                (byte[]) null
        )};

        PostRequestDTO requestDTO = null;

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
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
                .user(user)
                .privacy("public")
                .isDeleted(0)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Response result = postService.createPost(files, requestDTO);
        assertNotNull(result);
    }

    @Test
    void testCreatePost_FilesNull_RequestDtoNotNull() {
        MultipartFile[] files = null;

        PostRequestDTO requestDTO = new PostRequestDTO("test content");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
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
                .user(user)
                .text(requestDTO.getText())
                .privacy("public")
                .isDeleted(0)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Response result = postService.createPost(files, requestDTO);
        assertNotNull(result);
    }

    @Test
    void testEditPost_NotYourPost() {
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

        Post post = Post.builder()
                .id(1L)
                .user(user2)
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);
        Optional<Post> optionalPost = Optional.of(post);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            postService.editPost(post.getId(), null, null, null);
        });
    }

    @Test
    void testEditPost_DeletedPost() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        Post post = Post.builder()
                .id(1L)
                .user(user)
                .isDeleted(1)
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);
        Optional<Post> optionalPost = Optional.of(post);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            postService.editPost(post.getId(), null, null, null);
        });
    }

    @Test
    void testEditPost_TripleNull() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        Post post = Post.builder()
                .id(1L)
                .user(user)
                .isDeleted(0)
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);
        Optional<Post> optionalPost = Optional.of(post);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        assertThrows(GeneralException.class, () -> {
            postService.editPost(post.getId(), null, null, null);
        });
    }

    @Test
    void testEditPost_SuccessfulCase() {
        MultipartFile[] files = new MockMultipartFile[]{new MockMultipartFile(
                "fileName",
                (String) null,
                "image/jpeg",
                (byte[]) null
        )};
        PostRequestDTO requestDTO = new PostRequestDTO("test content");
        PostPrivacyDTO privacyDTO = new PostPrivacyDTO("public");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
                .username("test username")
                .role("USER")
                .build();

        Post post = Post.builder()
                .id(1L)
                .user(user)
                .isDeleted(0)
                .build();

        UserInfoUserDetails userDetails = new UserInfoUserDetails(user);

        Optional<User> optionalUser = Optional.of(user);
        Optional<Post> optionalPost = Optional.of(post);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(optionalUser);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        Response result = postService.editPost(post.getId(), files, requestDTO, privacyDTO);
        assertNotNull(result);
    }

    @Test
    void testGetAllPosts() {
        int offset = 0;
        int pageSize = 5;

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

        List<PostResponseDTO> result = postService.getAllPosts(offset, pageSize);

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

        assertThrows(GeneralException.class, () -> {
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

        assertThrows(GeneralException.class, () -> {
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

        assertThrows(GeneralException.class, () -> {
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

        assertThrows(GeneralException.class, () -> {
            postService.deletePost(post.getId());
        });
    }

    @Test
    void testDeletePost_SuccessfulCase() {
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
                .privacy("public")
                .build();

        Optional<Post> optionalPost = Optional.of(post);

        when(postRepository.findById(post.getId())).thenReturn(optionalPost);

        Comment comment = Comment.builder().build();
        Comment comment2 = Comment.builder().build();
        List<Comment> existComments = new ArrayList<>(Arrays.asList(comment, comment2));
        when(commentRepository.findAllByPostId(post.getId())).thenReturn(existComments);

        React react = React.builder().build();
        React react2 = React.builder().build();
        List<React> existReacts = new ArrayList<>(Arrays.asList(react, react2));
        when(reactRepository.findAllByPostId(post.getId())).thenReturn(existReacts);

        PostImage postImage = PostImage.builder().build();
        PostImage postImage2 = PostImage.builder().build();
        List<PostImage> existPostImage = new ArrayList<>(Arrays.asList(postImage, postImage2));
        when(postImageRepository.findAllByPostId(post.getId())).thenReturn(existPostImage);

        Response result = postService.deletePost(post.getId());
        assertNotNull(result);
    }

    @Test
    void testGetMyPosts() {
        int offset = 0;
        int pageSize = 5;

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        User user = User.builder()
                .id(1L)
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
                .user(user)
                .createdDate(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .user(user)
                .createdDate(LocalDateTime.now().minusMinutes(30))
                .build();

        List<Post> postList = new ArrayList<>(Arrays.asList(post, post2));
        when(postRepository.findAllByUserId(user.getId())).thenReturn(postList);

        PostImage postImage = PostImage.builder()
                .build();

        PostImage postImage2 = PostImage.builder()
                .build();

        List<PostImage> postImageList = new ArrayList<>(Arrays.asList(postImage, postImage2));
        when(postImageRepository.findAllByPostId(any())).thenReturn(postImageList);

        List<PostResponseDTO> result = postService.getMyPosts(offset, pageSize);

        assertNotNull(result);
    }
}