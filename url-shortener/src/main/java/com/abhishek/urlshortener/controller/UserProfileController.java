package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.ApiResponse;
import com.abhishek.urlshortener.dto.CountryDTO;
import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.security.JwtService;
import com.abhishek.urlshortener.service.CountryService;
import com.abhishek.urlshortener.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final CountryService countryService;
    private final JwtService jwtService;

    public UserProfileController(UserProfileService userProfileService,
                                 CountryService countryService, JwtService jwtService) {
        this.userProfileService = userProfileService;
        this.countryService = countryService;
        this.jwtService = jwtService;
    }

    @GetMapping("/profile-page-data")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfilePageData() {
        User user = jwtService.getAuthenticatedUser();
        String email = user.getEmail();
        UserDTO userDto = userProfileService.getUserProfile(email);

        List<CountryDTO> countries = countryService.getAllCountries();
        userDto.setCountryName(countryService.getCountryName(userDto.getCountry()));

        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        response.put("countries", countries);

        return ResponseEntity.ok(
                new ApiResponse<>(Status.SUCCESS, response, "Profile data loaded successfully")
        );
    }

    @PutMapping("/profile/update")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@RequestBody UserDTO dto) {
        User user = jwtService.getAuthenticatedUser();
        String email = user.getEmail();

        UserDTO updated = userProfileService.updateUserProfile(email, dto);
        updated.setCountryName(countryService.getCountryName(updated.getCountry()));

        return ResponseEntity.ok(
                new ApiResponse<>(Status.SUCCESS, updated, "Profile updated successfully")
        );
    }

}
