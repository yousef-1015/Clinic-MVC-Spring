package com.example.clinicmvcspring.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {
    // using JPQL finding appointments by status
    @Query("SELECT a FROM Appointment a WHERE a.status = :stat")
    Page<Appointment> findByStatus(@Param("stat") AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.status = :status AND a.dateAndTime BETWEEN :start AND :end") //TO AVOID N+1 PROBLEM
    List<Appointment> findUpcomingAppointments(@Param("status") AppointmentStatus status,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end);

}