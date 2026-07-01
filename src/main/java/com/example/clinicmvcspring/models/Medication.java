package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "medicationName", "createdAt" })
public class Medication {

    private int id;
    private String medicationName;
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
