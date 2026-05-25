package com.example.habittracker.auth.dto;

public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    AuthUser user
) {
    public record AuthUser(Long id, String email) {}
}
