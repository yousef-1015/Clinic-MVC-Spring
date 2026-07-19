package com.example.clinicmvcspring.dtos;

import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.RefreshToken;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "token", "refreshToken", "userDetails" })
public class AuthResponse {
    private final String token;
    private final CustomUserDetails userDetails;
    private final RefreshToken refreshToken;

    public AuthResponse(String token, CustomUserDetails userDetails, RefreshToken refreshToken) {
        this.token = token;
        this.userDetails = userDetails;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }

    public String getRefreshToken() {
        return refreshToken.getPlainTextToken();
    }

}
