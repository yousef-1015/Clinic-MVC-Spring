package com.example.clinicmvcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmvcspring.models.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer> {

}