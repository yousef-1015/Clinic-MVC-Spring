package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

public class AppointmentModel {

    private int id;
    private Timestamp dateAndTime;
    private int patientId;
    private int doctorId;
    private String status; // enum
    private Timestamp createdAt;

    // for adding
    public AppointmentModel(Timestamp dateAndTime, int patientId, int doctorId, String status) {
        this.dateAndTime = dateAndTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
    }

    // for getting
    public AppointmentModel(int id, Timestamp dateAndTime, int patientId, int doctorId, String status,
            Timestamp createdAt) {
        this.id = id;
        this.dateAndTime = dateAndTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public AppointmentModel() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                + " | Date & Time: " + dateAndTime
                + " | Patient ID: " + patientId
                + " | Doctor ID: " + doctorId
                + " | Status: " + status
                + " | Created: " + createdAt;
    }

}
