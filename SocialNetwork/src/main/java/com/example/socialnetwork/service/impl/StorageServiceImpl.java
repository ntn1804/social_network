package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.dto.response.Response;
import com.example.socialnetwork.entity.FileData;
import com.example.socialnetwork.repository.FileDataRepository;
import com.example.socialnetwork.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\";

    public Response uploadImageToFileSystem(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String stringUuid = uuid.toString();

        String filePath = folderPath + stringUuid;

        fileDataRepository.save(FileData.builder()
                        .name(stringUuid)
                        .type(file.getContentType())
                        .filePath(filePath)
                .build());

        file.transferTo(new File(filePath + ".jpg"));

        return Response.builder()
                .responseMessage("file uploaded successfully")
                .build();
    }

    public ResponseEntity<?> downloadImageFromFileSystem(String fileName) throws IOException {
        FileData fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }
}
