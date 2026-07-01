package com.example.clinicmvcspring.models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.*;

@JsonPropertyOrder({ "id", "firstName", "lastName", "email", "specialty", "hireDate" })
public class DoctorModel {

    // attributes
    private int id;

    @NotBlank(message = "First Name is Required")
    @Size(max = 50,message = "First Name max size is 50 characters")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max = 50,message = "Last Name max size is 50 characters")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Size(max = 100,message = "Email max size is 100 characters")
    @Email(message = "Use Valid Email")
    private String email;

    // decimal(6,2) in sql
    @DecimalMin(value = "0.0", message = "Salary must be 0 or greater")
    @Digits(integer = 4, fraction = 2, message = "Salary format: max 4 digits before decimal, 2 after ")
    private double salary;
    private Date hireDate;

    @NotBlank(message = "Specialty is Required")
    @Size(max = 50,message = "Specialty max size is 50 characters")
    private String specialty;

    // for creating
    public DoctorModel(String firstName, String lastName, String email, double salary, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.specialty = specialty;
    }

    // for getting
    public DoctorModel(int id, String firstName, String lastName, String email, double salary, Date hireDate,
            String specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
        this.specialty = specialty;
    }

    public DoctorModel() {
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: Dr. " + firstName + " " + lastName
                + " | Email: " + email
                + " | Salary: $" + salary
                + " | Hired: " + hireDate
                + " | Specialty: " + specialty;
    }

}
