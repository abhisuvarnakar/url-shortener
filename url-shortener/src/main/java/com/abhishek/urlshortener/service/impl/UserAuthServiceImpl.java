package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.exception.UserAlreadyExistsException;
import com.abhishek.urlshortener.repository.UserRepository;
import com.abhishek.urlshortener.security.JwtService;
import com.abhishek.urlshortener.service.UserAuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserAuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email '" + userDTO.getEmail() + "' " +
                    "already exists.");
        }

        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public String[] login(UserDTO userDTO) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(),
                        userDTO.getPassword()));

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user =
                userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        return jwtService.generateAccessToken(user);
    }


}
