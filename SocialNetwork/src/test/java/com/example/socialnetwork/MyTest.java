package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MyTest {

    @Test
    public void uploadFile() {
        MultipartFile file = new MockMultipartFile(
                "fileName",
                null,
                "text/plain",
                (byte[]) null
        );
        String fileName = file.getOriginalFilename();
        // You can use the file name to perform further operations
        System.out.println("Uploaded file name: " + fileName);
    }
}
