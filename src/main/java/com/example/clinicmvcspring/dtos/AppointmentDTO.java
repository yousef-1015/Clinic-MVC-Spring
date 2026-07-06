package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;
import com.example.clinicmvcspring.models.AppointmentStatus;

public class AppointmentDTO {
    private int id;
    private Timestamp dateAndTime;
    private int patientId;
    private int doctorId;
    private AppointmentStatus status;
    private Timestamp createdAt;
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
