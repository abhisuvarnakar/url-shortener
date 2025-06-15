package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.repository.UserRepository;
import com.abhishek.urlshortener.service.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserProfileServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDTO getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUserProfile(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setGender(userDTO.getGender());
        user.setPhone(userDTO.getPhone());
        user.setCity(userDTO.getCity());
        user.setCountry(userDTO.getCountry());
        user.setBio(userDTO.getBio());

        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }
}
