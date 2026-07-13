package com.example.clinicmvcspring.dtos;

import com.example.clinicmvcspring.models.CustomUserDetails;

public class AuthResponse {
    private final String token;
    private final CustomUserDetails userDetails;

    public AuthResponse(String token, CustomUserDetails userDetails) {
        this.token = token;
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }
}
