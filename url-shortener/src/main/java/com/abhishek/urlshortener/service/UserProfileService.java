package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UserDTO;

public interface UserProfileService {

    UserDTO getUserProfile(String email);

    UserDTO updateUserProfile(String email, UserDTO userDTO);

}
