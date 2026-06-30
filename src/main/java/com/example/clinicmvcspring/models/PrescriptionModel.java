package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

public class PrescriptionModel {

    private int id;
    private String prescriptionNotes;
    private int appointmentId;
    private Timestamp createdAt;

    // for adding
    public PrescriptionModel(String prescriptionNotes, int appointmentId) {
        this.prescriptionNotes = prescriptionNotes;
        this.appointmentId = appointmentId;
    }

    // for getting
    public PrescriptionModel(int id, String prescriptionNotes, int appointmentId, Timestamp createdAt) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.appointmentId = appointmentId;
        this.createdAt = createdAt;
    }

    public PrescriptionModel() {
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

    @Override
    public String toString() {
        return "ID: " + id
                + " | Notes: " + prescriptionNotes
                + " | Appointment ID: " + appointmentId
                + " | Created: " + createdAt;
    }

}
