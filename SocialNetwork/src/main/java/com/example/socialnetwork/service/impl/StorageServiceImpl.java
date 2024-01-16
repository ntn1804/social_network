package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.entity.FileData;
import com.example.socialnetwork.entity.ImageData;
import com.example.socialnetwork.repository.FileDataRepo;
import com.example.socialnetwork.repository.StorageRepo;
import com.example.socialnetwork.service.StorageService;
import com.example.socialnetwork.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepo storageRepo;

    @Autowired
    private FileDataRepo fileDataRepo;

    private final String folderPath = "C:\\Users\\nguyentrungnghia\\Desktop\\MyFiles\\";

    public ResponseEntity<?> uploadImage(MultipartFile file) throws IOException {
        try {
            ImageData imageData = storageRepo.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build());
            if (imageData != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body("file uploaded successfully: " + file.getOriginalFilename());
            }
            return null;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("fail to upload image: " + file.getOriginalFilename());
        }
    }

    public ResponseEntity<?> downloadImage(String fileName) {
        Optional<ImageData> dbImageData = storageRepo.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    public ResponseEntity<?> uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = folderPath + file.getOriginalFilename();
        FileData existingFileData = fileDataRepo.findByFilePath(filePath);
        if (existingFileData != null){
            fileDataRepo.delete(existingFileData);
        }
        FileData fileData = fileDataRepo.save(FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                .build());

        file.transferTo(new File(filePath));

        if(fileData != null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("file uploaded successfully: " + file.getOriginalFilename());
        }
        return null;
    }

    public ResponseEntity<?> downloadImageFromFileSystem(String fileName) throws IOException {
        FileData fileData = fileDataRepo.findByName(fileName);
        String filePath = fileData.getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }
}
