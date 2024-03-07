package com.example.socialnetwork.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void imageUploadUtil(MultipartFile multipartFile) throws IOException {
        Path folder = Paths.get("src/main/resources/static/images");
        String filename = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        Path file = Files.createTempFile(folder, filename + "-", "." + extension);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
