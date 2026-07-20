package com.example.clinicmvcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmvcspring.models.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Integer> {

}
