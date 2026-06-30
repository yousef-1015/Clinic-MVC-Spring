package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

public class MedicationModel {

    private int id;
    private String medicationName;
    private Timestamp createdAt;

    // for adding
    public MedicationModel(String medicationName) {
        this.medicationName = medicationName;
    }

    // for getting
    public MedicationModel(int id, String medicationName, Timestamp createdAt) {
        this.id = id;
        this.medicationName = medicationName;
        this.createdAt = createdAt;
    }

    public MedicationModel() {
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
