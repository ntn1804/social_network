package com.example.socialnetwork.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAPI {

    @GetMapping("/test")
    public String testAPI() {
        return "hihi";
    }
}
