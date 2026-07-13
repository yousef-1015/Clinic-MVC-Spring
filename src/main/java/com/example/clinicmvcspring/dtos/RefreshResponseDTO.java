package com.example.clinicmvcspring.dtos;

public class RefreshResponseDTO {
    private String newAccessToken;

    public RefreshResponseDTO(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }

    public String getNewAccessToken() {
        return newAccessToken;
    }

    public void setNewAccessToken(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }


}
