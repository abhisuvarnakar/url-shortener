package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.LoginResponseDTO;
import com.abhishek.urlshortener.dto.UserDTO;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.UserAlreadyExistsException;
import com.abhishek.urlshortener.service.AuthService;
import com.abhishek.urlshortener.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    public AuthController(AuthService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody UserDTO userDto) {
        Map<String, String> response;

        if (!Utils.isValidEmail(userDto.getEmail())) {
            response = getResponse("Valid email is required", Status.FAILED.name());
            return ResponseEntity.badRequest().body(response);
        }

        if (StringUtils.isBlank(userDto.getPassword())) {
            response = getResponse("Password is required", Status.FAILED.name());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User user = userService.createUser(userDto);
            response = getResponse("Registration successful. User Id created: " + user.getId(),
                    Status.SUCCESS.name());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            response = getResponse(e.getMessage(), Status.FAILED.name());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO userDTO,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        try {
            String[] tokens = userService.login(userDTO);

            Cookie accessCookie = new Cookie("jwtToken", tokens[0]);
            accessCookie.setHttpOnly(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(10 * 10 * 60);
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", tokens[1]);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(new LoginResponseDTO("SUCCESS", tokens[0], "Login " +
                    "successful"));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("FAILED", null, "Invalid email or password."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO("FAILED", null, "Something went wrong. Please try " +
                            "again later."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request,
                                                    HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found " +
                        "inside the Cookies"));

        String accessToken = userService.refreshToken(refreshToken);

        Cookie accessCookie = new Cookie("jwtToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(10 * 60);
        response.addCookie(accessCookie);


        return ResponseEntity.ok(new LoginResponseDTO(Status.SUCCESS.name(), accessToken, "Token " +
                "Refreshed."));
    }

    private Map<String, String> getResponse(String message, String status) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);

        return response;
    }
}
