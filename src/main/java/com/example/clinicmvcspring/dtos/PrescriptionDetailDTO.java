package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;
import java.util.List;

public class PrescriptionDetailDTO {
    private int id;
    private String prescriptionNotes;
    private int appointmentId;
    private Timestamp createdAt;
    private List<PrescriptionMedicationDTO> medications;

    public PrescriptionDetailDTO() {
    }

    public PrescriptionDetailDTO(int id, String prescriptionNotes, int appointmentId, Timestamp createdAt,
            List<PrescriptionMedicationDTO> medications) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.appointmentId = appointmentId;
        this.createdAt = createdAt;
        this.medications = medications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrescriptionNotes() {
        return prescriptionNotes;
    }

    public void setPrescriptionNotes(String prescriptionNotes) {
        this.prescriptionNotes = prescriptionNotes;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
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
