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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "medications")
@JsonPropertyOrder({ "id", "medicationName", "createdAt" })
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the medication", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "Medication Name is Required")
    @Size(max = 100, message = "Medication Name max size is 100 characters")
    @Column(name = "medication_name")
    @Schema(description = "Name of the medication", example = "Panadol", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medicationName;

    @Column(name = "created_at")
    @CreationTimestamp
    @Schema(description = "Timestamp when the medication was added", example = "2026-07-22T10:00:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    // for adding
    public Medication(String medicationName) {
        this.medicationName = medicationName;
    }

    // for getting
    public Medication(int id, String medicationName, Timestamp createdAt) {
        this.id = id;
        this.medicationName = medicationName;
        this.createdAt = createdAt;
    }

    public Medication() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
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
                + " | Medication: " + medicationName
                + " | Added: " + createdAt;
    }

}
