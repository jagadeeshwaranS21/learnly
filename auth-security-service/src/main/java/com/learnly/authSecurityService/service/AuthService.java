package com.learnly.authSecurityService.service;

import com.learnly.authSecurityService.dto.AuthResponse;
import com.learnly.authSecurityService.dto.TokenRefreshRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthService {

    Mono<AuthResponse> login(String email, String password);
    Mono<AuthResponse> refreshAccessToken(TokenRefreshRequest request);
    List<String> fetchRolesOfUserFromAccessToken(String accessToken);
}
