package com.example.clinicmvcspring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> ,JpaSpecificationExecutor<Appointment>{
    // using JPQL finding appointments by status
    @Query("SELECT a FROM Appointment a WHERE a.status = :stat")
    Page<Appointment> findByStatus(@Param("stat") AppointmentStatus status, Pageable pageable);

}