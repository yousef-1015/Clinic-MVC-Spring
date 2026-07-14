package com.example.clinicmvcspring.dtos;

public class RefreshResponseDTO {
    private String newAccessToken;
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
