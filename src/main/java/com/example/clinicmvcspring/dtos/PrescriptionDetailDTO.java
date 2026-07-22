package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrescriptionDetailDTO {
    @Schema(description = "Unique ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Doctor's notes and dosage instructions for the prescription", example = "Take 1 pill twice daily after meals for 7 days")
    private String prescriptionNotes;

    @Schema(description = "ID of the associated appointment", example = "10")
    private Integer appointmentId;

    @Schema(description = "Creation timestamp", example = "2026-07-22T10:00:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "List of prescribed medications and dosage details")
    private List<PrescriptionMedicationDTO> medications;

    public PrescriptionDetailDTO() {
    }

    public PrescriptionDetailDTO(Integer id, String prescriptionNotes, Integer appointmentId, Timestamp createdAt,
            List<PrescriptionMedicationDTO> medications) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.appointmentId = appointmentId;
        this.createdAt = createdAt;
        this.medications = medications;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrescriptionNotes() {
        return prescriptionNotes;
    }

    public void setPrescriptionNotes(String prescriptionNotes) {
        this.prescriptionNotes = prescriptionNotes;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<PrescriptionMedicationDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<PrescriptionMedicationDTO> medications) {
        this.medications = medications;
    }
}
