package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appointments")
@JsonPropertyOrder({ "id", "dateAndTime", "patientId", "doctorId", "status", "createdAt" })

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Date and Time is Required")
    @Column(name = "date_and_time")
    private Timestamp dateAndTime;

    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "doctor_id")
    private int doctorId;

    @NotNull(message = "Status is Required")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // enum

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    // for adding
    public Appointment(Timestamp dateAndTime, int patientId, int doctorId, AppointmentStatus status) {
        this.dateAndTime = dateAndTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
    }

    // for getting
    public Appointment(int id, Timestamp dateAndTime, int patientId, int doctorId, AppointmentStatus status,
            Timestamp createdAt) {
        this.id = id;
        this.dateAndTime = dateAndTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Appointment() {
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
