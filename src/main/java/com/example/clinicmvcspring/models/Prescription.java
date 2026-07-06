package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "prescriptions")
@JsonPropertyOrder({ "id", "prescriptionNotes", "appointmentId", "createdAt" })
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 200, message = "Prescription notes max size is 200 characters")
    @Column(name = "prescription_notes")
    private String prescriptionNotes;

    // owning entity
    @OneToOne // every prescription has one appointment
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    
    // for adding

    public Prescription(String prescriptionNotes, Appointment appointment) {
        this.prescriptionNotes = prescriptionNotes;
        this.appointment = appointment;
    }

    // for getting
    public Prescription(int id, String prescriptionNotes, Appointment appointment, Timestamp createdAt) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.appointment = appointment;
        this.createdAt = createdAt;
    }

    public Prescription() {
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("appointmentId")
    public Integer getAppointmentId() {
        return this.appointment != null ? this.appointment.getId() : null;
    }

    @JsonProperty("appointmentId")
    public void setAppointmentId(Integer appointmentId) {
        if (appointmentId != null) {
            this.appointment = new Appointment();
            this.appointment.setId(appointmentId);
        }
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Notes: " + prescriptionNotes
                + " | Appointment ID: " + appointment
                + " | Created: " + createdAt;
    }

}
