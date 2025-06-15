package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;

public interface AuthService {

    User createUser(UserDTO userDTO);

    String[] login(UserDTO userDTO);

    String refreshToken(String refreshToken);
}
