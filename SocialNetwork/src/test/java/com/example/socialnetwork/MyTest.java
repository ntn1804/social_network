package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MyTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void readResourceFile() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/images/default-avatar.jpg");
        var image = resource.getInputStream();
        System.out.println(image);
    }
}
