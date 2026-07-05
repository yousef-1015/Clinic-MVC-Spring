package com.example.clinicmvcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmvcspring.models.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

}