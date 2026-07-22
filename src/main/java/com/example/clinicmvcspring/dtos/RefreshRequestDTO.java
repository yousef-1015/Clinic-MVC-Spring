package com.example.clinicmvcspring.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class RefreshRequestDTO {
    @Schema(description = "Refresh token string generated during login", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
