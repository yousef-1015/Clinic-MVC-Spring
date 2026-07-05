package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "medications")
@JsonPropertyOrder({ "id", "medicationName", "createdAt" })
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Medication Name is Required")
    @Size(max = 100, message = "Medication Name max size is 100 characters")
    @Column(name = "medication_name")
    private String medicationName;

    @Column(name = "created_at")
    @CreationTimestamp
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
