package com.example.clinicmvcspring.models;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
// name in db
@Table(name = "doctors")
@JsonPropertyOrder({ "id", "firstName", "lastName", "email", "specialty", "hireDate" })
public class Doctor {

    // attributes
    @Id // needed fo each table
    // the id is auto generated in my db
    // we used GenerationType.IDENTITY to tell jpa that the value is auto generated
    // by the db don't do it yourself
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First Name is Required")
    @Size(max = 50, message = "First Name max size is 50 characters")
    @Column(name = "first_name") // name in db
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max = 50, message = "Last Name max size is 50 characters")
    @Column(name = "last_name") // name in db
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Size(max = 100, message = "Email max size is 100 characters")
    @Email(message = "Use Valid Email")
    @Column(name = "email") // name in db
    private String email;

    // decimal(6,2) in sql
    @DecimalMin(value = "0.0", message = "Salary must be 0 or greater")
    @Digits(integer = 4, fraction = 2, message = "Salary format: max 4 digits before decimal, 2 after ")
    @Column(name = "salary", precision = 6, scale = 2)
    // name in db (NOT NEEDED HERE BECAUSE THE COLUMN NAME IS THE SAME AS THE
    // ATTRIBUTE NAME)
    // changed to BigDecimal so i can use the @Digits validation with jpa
    // (hibernate)
    private BigDecimal salary;

    @Column(name = "hire_date") // name in db
    private Date hireDate;

    @NotBlank(message = "Specialty is Required")
    @Size(max = 50, message = "Specialty max size is 50 characters")
    @Column(name = "specialty") // name in db (NOT NEEDED HERE BECAUSE THE COLUMN NAME IS THE SAME AS THE
    // ATTRIBUTE NAME)
    private String specialty;

    // for creating
    public Doctor(String firstName, String lastName, String email, BigDecimal salary, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.specialty = specialty;
    }

    // for getting
    public Doctor(int id, String firstName, String lastName, String email, BigDecimal salary, Date hireDate,
            String specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
        this.specialty = specialty;
    }

    public Doctor() {
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
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
