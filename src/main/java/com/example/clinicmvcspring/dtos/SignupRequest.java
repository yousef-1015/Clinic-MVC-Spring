package com.example.clinicmvcspring.dtos;

import com.example.clinicmvcspring.models.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public class SignupRequest {
    @Schema(description = "Username for the new account", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "Password for the new account", example = "secret123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(description = "Role assigned to the user (e.g. ADMIN, DOCTOR)", example = "DOCTOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;

    @Schema(description = "Optional Doctor ID to link if the role is DOCTOR", example = "1")
    private Integer foreignId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getForeignId() {
        return foreignId;
    }

    public void setForeignId(Integer foreignId) {
        this.foreignId = foreignId;
    }

}
