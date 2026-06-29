package com.example.clinicmvcspring.models;

import java.sql.Date;

public class DoctorModel {

    // attributes
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private double salary;
    private Date hireDate;
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
