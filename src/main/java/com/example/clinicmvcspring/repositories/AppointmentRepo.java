package com.example.clinicmvcspring.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.models.Doctor;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {
    // using JPQL finding appointments by status
    @Query("SELECT a FROM Appointment a WHERE a.status = :stat")
    Page<Appointment> findByStatus(@Param("stat") AppointmentStatus status, Pageable pageable);

    // JOIN FETCH to avoid N+1 problem
    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.status = :status AND a.dateAndTime BETWEEN :start AND :end") // PROBLEM
    List<Appointment> findUpcomingAppointments(@Param("status") AppointmentStatus status,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a WHERE a.doctor = :doctor AND a.dateAndTime = :time AND a.status != 'Cancelled'")
    boolean isDoctorBooked(@Param("doctor") Doctor doctor, @Param("time") Timestamp time);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.status IN :statuses AND a.dateAndTime < :cutoffDate")
    void deleteOldAppointments(@Param("statuses") List<AppointmentStatus> statuses,
            @Param("cutoffDate") Timestamp cutoffDate);

}