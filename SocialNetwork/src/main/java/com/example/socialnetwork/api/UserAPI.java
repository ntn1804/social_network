package com.example.socialnetwork.api;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.IUserService;
import com.example.socialnetwork.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserAPI {

    @Autowired
    private IUserService userService;
    @PostMapping("/register")
    public String registration(@RequestBody UserDTO userDTO) {
        return userService.registration(userDTO);
    }


}
