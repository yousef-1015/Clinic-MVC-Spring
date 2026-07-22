package com.example.clinicmvcspring.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrescriptionMedicationDTO {

    @Schema(description = "Medication name", example = "Panadol", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicationName;
    @Schema(description = "ID of the medication", example = "1")
    private int medicationId;
    @Schema(description = "Dosage instructions", example = "500mg")
    private String dosage;
    @Schema(description = "Frequency instructions", example = "Twice daily after meals")
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
