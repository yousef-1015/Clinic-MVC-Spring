package com.example.clinicmvcspring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.clinicmvcspring.models.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Integer>,JpaSpecificationExecutor<Doctor> {
    // JPQL query to find Doctors by specialty with pagination
    @Query("SELECT d FROM Doctor d WHERE d.specialty = :spec")
    Page<Doctor> findBySpecialty(@Param("spec") String specialty, Pageable pageable);
}