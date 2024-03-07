package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void readResourceFile() throws IOException {
//
//        String name = "file";
//        String disposition = "form-data; name=\"" + name + "\"; filename=\"myFile.txt\"";
//        StandardMultipartHttpServletRequest request = requestWithPart(name, disposition, "myBody");
//        MultipartFile multipartFile = request.getFile(name);
//        assertNotNull(multipartFile);
    }
}
