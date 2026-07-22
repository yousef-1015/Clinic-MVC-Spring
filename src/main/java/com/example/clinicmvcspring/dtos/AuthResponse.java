package com.example.clinicmvcspring.dtos;

import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.RefreshToken;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "token", "refreshToken", "userDetails" })
public class AuthResponse {
    @Schema(description = "JWT Access Token for authorization", example = "eyJhbGciOiJIUzI1NiJ9...")
    private final String token;
    @Schema(description = "Authenticated user profile details")
    private final CustomUserDetails userDetails;
    @Schema(description = "Refresh token for generating new access tokens", example = "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
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
