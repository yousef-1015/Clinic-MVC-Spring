package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;
import java.util.List;

public class PrescriptionDetailDTO {
    private Integer id;
    private String prescriptionNotes;
    private Integer appointmentId;
    private Timestamp createdAt;
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
