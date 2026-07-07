package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    // @Column(name = "patient_id")
    // private int patientId;

    // @Column(name = "doctor_id")
    // private int doctorId;;

    @ManyToOne(fetch = FetchType.EAGER)  // one patient many appointments
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER) // one doctor many appointments
    // NO CASCADE HERE
    @JoinColumn(name = "doctor_id") // foreign key at many side
    @JsonIgnore // don't show all doctor info like salary and hire date they don't belong here
    private Doctor doctor;

    @NotNull(message = "Status is Required")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // enum

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
//fetch type is EAGER by default
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    private Prescription prescription;

    // for adding
    public Appointment(Timestamp dateAndTime, Patient patient, Doctor doctor, AppointmentStatus status) {
        this.dateAndTime = dateAndTime;
        this.patient = patient;
        this.doctor = doctor;
        this.status = status;
    }

    // for getting
    public Appointment(int id, Timestamp dateAndTime, Patient patient, Doctor doctor, AppointmentStatus status,
            Timestamp createdAt) {
        this.id = id;
        this.dateAndTime = dateAndTime;
        this.patient = patient;
        this.doctor = doctor;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @JsonProperty("doctorId")
    public Integer getDoctorId() {
        return this.doctor != null ? this.doctor.getId() : null;
    }

    @JsonProperty("doctorId")
    public void setDoctorId(Integer doctorId) {
        if (doctorId != null) {
            this.doctor = new Doctor();
            this.doctor.setId(doctorId);
        }
    }

    @JsonProperty("patientId")
    public Integer getPatientId() {
        return this.patient != null ? this.patient.getId() : null;
    }

    @JsonProperty("patientId")
    public void setPatientId(Integer patientId) {
        if (patientId != null) {
            this.patient = new Patient();
            this.patient.setId(patientId);
        }
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Date & Time: " + dateAndTime
                + " | Patient ID: " + patient.getId()
                + " | Doctor ID: " + doctor.getId()
                + " | Status: " + status
                + " | Created: " + createdAt;
    }

}
