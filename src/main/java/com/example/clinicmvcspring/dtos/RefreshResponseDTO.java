package com.example.clinicmvcspring.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class RefreshResponseDTO {
    @Schema(description = "New JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String newAccessToken;
    @Schema(description = "New UUID refresh token", example = "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
    private String newRefreshToken;

    public RefreshResponseDTO(String newAccessToken, String newRefreshToken) {
        this.newAccessToken = newAccessToken;
        this.newRefreshToken = newRefreshToken;
    }

    public String getNewAccessToken() {
        return newAccessToken;
    }

    public void setNewAccessToken(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }

    public String getNewRefreshToken() {
        return newRefreshToken;
    }

    public void setNewRefreshToken(String newRefreshToken) {
        this.newRefreshToken = newRefreshToken;
    }

}
