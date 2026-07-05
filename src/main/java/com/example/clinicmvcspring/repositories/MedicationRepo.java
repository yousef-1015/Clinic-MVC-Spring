package com.example.clinicmvcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmvcspring.models.Medication;

public interface MedicationRepo extends JpaRepository<Medication, Integer> {

}