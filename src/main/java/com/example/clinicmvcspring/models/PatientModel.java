package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

public class PatientModel {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp createdAt;

    // for creating
    public PatientModel(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // for getting
    public PatientModel(int id, String firstName, String lastName, String email, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                + " | Name: " + firstName + " " + lastName
                + " | Email: " + email
                + " | Registered: " + createdAt;
    }

}
