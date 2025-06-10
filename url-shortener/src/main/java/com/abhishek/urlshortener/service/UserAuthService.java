package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;

public interface UserAuthService {

    User createUser(UserDTO userDTO);

    String[] login(UserDTO userDTO);

    String refreshToken(String refreshToken);
}
