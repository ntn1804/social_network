package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.entity.ImageData;
import com.example.socialnetwork.repository.StorageRepo;
import com.example.socialnetwork.service.StorageService;
import com.example.socialnetwork.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepo storageRepo;

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
                    .body("Ko up dc" + file.getOriginalFilename());
        }

    }

    public ResponseEntity<?> downloadImage(String fileName) {
        Optional<ImageData> dbImageData = storageRepo.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }
}
