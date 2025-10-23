package com.learnly.authSecurityService.service.impl;

import com.learnly.authSecurityService.dto.AuthResponse;
import com.learnly.authSecurityService.dto.TokenRefreshRequest;
import com.learnly.authSecurityService.dto.UserResponse;
import com.learnly.authSecurityService.model.RefreshToken;
import com.learnly.authSecurityService.repository.RefreshTokenRepository;
import com.learnly.authSecurityService.service.AuthService;
import com.learnly.authSecurityService.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private final WebClient.Builder webClient;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh.expiration:604800000}")
    private long refreshTokenExpiration;

    @Override
    public Mono<AuthResponse> login(String email, String password) {
        return webClient.
                build()
                .get()
                .uri("/api/users/findUserByEmail/{email}", email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .flatMap(user -> {

                    if (!user.isActive()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is inactive"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
                    }
                    List<String> roles = List.of(user.getRole());
                    String accessToken = jwtUtil.generateToken(email, roles);
                    String refreshToken = jwtUtil.generateRefreshToken(email);
                    RefreshToken tokenEntity = new RefreshToken();
                    tokenEntity.setUserId(user.getUserId());
                    tokenEntity.setToken(refreshToken);
                    tokenEntity.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
                    tokenEntity.setRevoked(false);
                    refreshTokenRepository.save(tokenEntity);
                    return Mono.just(new AuthResponse(accessToken, refreshToken));

                });
    }

    /**
     * refreshAccessToken(TokenRefreshRequest request)
     * fetching user from user-service for each refreshAccessToken call
     * because what if the user role changes in user db and still we saved the role of the user as something
     * data will be inconsistency
     */

    @Override
    public Mono<AuthResponse> refreshAccessToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        RefreshToken tokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (!jwtUtil.isTokenValid(refreshToken)) {
            return Mono.error(new RuntimeException("Invalid or expired refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken);

        return webClient.build()
                .get()
                .uri("/api/users/findUserByEmail/{email}", username)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .flatMap(user -> {

                    List<String> roles = List.of(user.getRole());

                    String newAccessToken = jwtUtil.generateToken(username, roles);
                    String newRefreshToken = jwtUtil.generateRefreshToken(username);

                    tokenEntity.setRevoked(true);
                    refreshTokenRepository.save(tokenEntity);

                    RefreshToken newTokenEntity = new RefreshToken();
                    newTokenEntity.setUserId(tokenEntity.getUserId());
                    newTokenEntity.setToken(newRefreshToken);
                    newTokenEntity.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
                    newTokenEntity.setRevoked(false);
                    refreshTokenRepository.save(newTokenEntity);

                    return Mono.just(new AuthResponse(newAccessToken, newRefreshToken));
                });
    }

    @Override
    public List<String> fetchRolesOfUserFromAccessToken(String accessToken) {
        try {
            if (!jwtUtil.isTokenValid(accessToken)) {
                throw new RuntimeException("Invalid or expired token");
            }
            return jwtUtil.extractRoles(accessToken);
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract roles from token");
        }
    }

}
