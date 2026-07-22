package com.example.clinicmvcspring.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class DoctorDTO {
    @Schema(description = "Unique ID of the doctor", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
    @Schema(description = "Doctor's first name", example = "sampleFirstName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;
    @Schema(description = "Doctor's last name", example = "sampleLastName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;
    @Schema(description = "Doctor's Email", example = "example@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "Doctor's Specialty", example = "surgeon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specialty;

    public DoctorDTO() {
    }

    public DoctorDTO(int id, String firstName, String lastName, String email, String specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialty = specialty;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
