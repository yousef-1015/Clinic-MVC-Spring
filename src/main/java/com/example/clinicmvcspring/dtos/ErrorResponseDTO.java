package com.example.clinicmvcspring.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponseDTO {
    @Schema(description = "Error message details", example = "Page Number Must Be Positive")
    private String message;
    @Schema(description = "HTTP Status Code", example = "400")
    private int status;

    public ErrorResponseDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
