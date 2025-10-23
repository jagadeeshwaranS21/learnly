package com.learnly.authSecurityService.controller;

import com.learnly.authSecurityService.dto.AuthResponse;
import com.learnly.authSecurityService.dto.LoginRequest;
import com.learnly.authSecurityService.dto.TokenRefreshRequest;
import com.learnly.authSecurityService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody LoginRequest request) {
            return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public Mono<AuthResponse> refresh(@RequestBody TokenRefreshRequest request) {
        return authService.refreshAccessToken(request);
    }

    @GetMapping("/roles")
    public List<String> getRoles(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return authService.fetchRolesOfUserFromAccessToken(token);
    }

}


