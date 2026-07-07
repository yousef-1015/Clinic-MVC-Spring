package com.example.clinicmvcspring.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;
import com.example.clinicmvcspring.specifications.DoctorSpecifications;

@Service
public class DoctorService {

    private final DoctorRepo repo;

    public DoctorService(DoctorRepo repo) {
        this.repo = repo;
    }

    public Doctor addDoctor(Doctor newDoctor) {
        return repo.save(newDoctor);// using the .save from the built in methods in JpaRepositories
    }

    public List<Doctor> getAllDoctors() {
        return repo.findAll();// the job lies on the repo to access the DB
    }

    public void deleteDoctor(Doctor docToDelete) {
        repo.delete(docToDelete);
    }

    public Optional<Doctor> getDoctorByID(int id) {
        return repo.findById(id);
    }

    public void deleteDoctorByID(int id) {
        repo.deleteById(id);
    }

    public Doctor updateDoctor(Doctor doc) {
        return repo.save(doc);
    }

    public Doctor updateDoctorById(int id, Doctor doc) {
        doc.setId(id);
        return repo.save(doc);
    }

    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Doctor> findDoctorsBySpecialty(String specialty, Pageable pageable) {
        return repo.findBySpecialty(specialty, pageable);
    }

    public Page<Doctor> findDoctorsBySpecialtyAndSalary(String specialty, BigDecimal salary, Pageable pageable) {

        // Combine specifications
        Specification<Doctor> spec = Specification
                .where(DoctorSpecifications.hasSpecialty(specialty))
                .and(DoctorSpecifications.salaryGreaterThan(salary));

        
        return repo.findAll(spec, pageable);
    }
}
