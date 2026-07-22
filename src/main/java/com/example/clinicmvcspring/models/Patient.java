package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "patients")
@JsonPropertyOrder({ "id", "firstName", "lastName", "email" })

public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the patient", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "First Name is Required")
    @Size(max = 50, message = "First Name max size is 50 characters")
    @Column(name = "first_name")
    @Schema(description = "Patient's first name", example = "sampleFirstName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max = 50, message = "Last Name max size is 50 characters")
    @Column(name = "last_name")
    @Schema(description = "Patient's last name", example = "sampleLastName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Size(max = 100, message = "Email max size is 100 characters")
    @Email(message = "Use Valid Email")
    @Column(name = "email")
    @Schema(description = "Patient's email address", example = "example@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Column(name = "created_at")
    @CreationTimestamp
    @Schema(description = "Timestamp when the patient was registered", example = "2026-07-22T10:00:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    // for creating
    public Patient(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // for getting
    public Patient(int id, String firstName, String lastName, String email, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Patient() {
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + firstName + " " + lastName
                + " | Email: " + email
                + " | Registered: " + createdAt;
    }

}
