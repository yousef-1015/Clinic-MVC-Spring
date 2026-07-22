package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;

import com.example.clinicmvcspring.models.AppointmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public class AppointmentDTO {
    @Schema(description = "Unique ID of the appointment", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Scheduled date and time of the appointment", example = "2026-07-25T14:30:00Z")
    private Timestamp dateAndTime;

    @Schema(description = "ID of the patient", example = "5")
    private int patientId;

    @Schema(description = "ID of the assigned doctor", example = "3")
    private int doctorId;

    @Schema(description = "Current appointment status", example = "SCHEDULED")
    private AppointmentStatus status;

@Schema(description = "Timestamp when the appointment record was created", example = "2026-07-22T10:00:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(description = "Prescription details")
    private PrescriptionDTO prescription; // PrescriptionDTO WITH ONLY THE INFO I NEED WHEN SHOWING APPOINTMENTS

    public AppointmentDTO() {
    }

    public AppointmentDTO(int id, Timestamp dateAndTime, int patientId, int doctorId,
            AppointmentStatus status, Timestamp createdAt, PrescriptionDTO prescription) {
        this.id = id;
        this.dateAndTime = dateAndTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.createdAt = createdAt;
        this.prescription = prescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }
}
