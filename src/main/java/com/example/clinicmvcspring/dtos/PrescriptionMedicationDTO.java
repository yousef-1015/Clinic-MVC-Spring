package com.example.clinicmvcspring.dtos;

public class PrescriptionMedicationDTO {
    private String medicationName;
    private int medicationId;
    private String dosage;
    private String frequency;

    public PrescriptionMedicationDTO() {
    }

    public PrescriptionMedicationDTO(int medicationId, String medicationName, String dosage, String frequency) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
