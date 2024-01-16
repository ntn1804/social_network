package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.dto.request.PostRequestDTO;
import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.FileData;
import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.FileDataRepo;
import com.example.socialnetwork.repository.PostRepo;
import com.example.socialnetwork.repository.UserRepo;
import com.example.socialnetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\Post\\";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private FileDataRepo fileDataRepo;
    @Override
    public ResponseEntity<Response> createPost(MultipartFile file, PostRequestDTO requestDTO) {
        // use jwt to get username.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepo.findByUsername(userDetails.getUsername());

        Post post = Post.builder()
                .text(requestDTO.getText())
                .image(file.getOriginalFilename())
                .user(user.get())
                .build();
        postRepo.save(post);
        try {
            savePostImageToFileSystem(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Created post successfully")
                .build());
    }

    public void savePostImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = folderPath + file.getOriginalFilename();
        FileData existingFileData = fileDataRepo.findByFilePath(filePath);
        if (existingFileData != null){
            fileDataRepo.delete(existingFileData);
        }

        FileData fileData = FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build();

        fileDataRepo.save(fileData);
        file.transferTo(new File(filePath));
    }
}
