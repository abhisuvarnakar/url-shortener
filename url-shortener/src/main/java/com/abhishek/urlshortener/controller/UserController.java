package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.service.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserAuthService userService;

    public UserController(UserAuthService userService) {
        this.userService = userService;
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUserData(@RequestBody UserDTO userDTO) {

        return null;
    }
}
