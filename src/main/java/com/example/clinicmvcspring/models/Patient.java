package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({ "id", "firstName", "lastName", "email" })

public class Patient {

    private int id;
    @NotBlank(message = "First Name is Required")
    @Size(max = 50, message = "First Name max size is 50 characters")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max = 50, message = "Last Name max size is 50 characters")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Size(max = 100, message = "Email max size is 100 characters")
    @Email(message = "Use Valid Email")
    private String email;

    private Timestamp createdAt;

    // for creating
    public Patient(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // for getting
    public Patient(int id, String firstName, String lastName, String email, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Patient() {
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
