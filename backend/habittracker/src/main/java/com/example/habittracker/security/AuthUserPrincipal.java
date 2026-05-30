package com.example.habittracker.security;

import java.security.Principal;

public record AuthUserPrincipal(Long userId, String email) implements Principal {
    @Override
    public String getName() {
        return email;
    }
}
