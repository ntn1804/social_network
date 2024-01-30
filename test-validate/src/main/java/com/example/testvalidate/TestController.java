package com.example.testvalidate;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {
    @PostMapping("/test")
    public ResponseEntity<String> resgistration(@Valid @RequestBody RegistrationRequestDTO request) {
        return ResponseEntity.ok("ok");
    }
}
