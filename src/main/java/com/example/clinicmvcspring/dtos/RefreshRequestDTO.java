package com.example.clinicmvcspring.dtos;

public class RefreshRequestDTO {
    private String refreshToken;
    

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
