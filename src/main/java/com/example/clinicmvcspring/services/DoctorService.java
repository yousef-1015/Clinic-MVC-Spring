package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class DoctorService {

    private final DoctorRepo repo;

    public DoctorService(DoctorRepo repo) {
        this.repo = repo;
    }

    public boolean addDoctor(Doctor newDoctor) {
        return repo.insert(newDoctor);// the job lies on the repo to access the DB
    }

    public List<Doctor> getAllDoctors() {
        return repo.findAll();// the job lies on the repo to access the DB
    }

    public boolean deleteDoctor(Doctor docToDelete) {
        return repo.deleteObj(docToDelete);
    }

    public Doctor getDoctorByID(int id) {
        return repo.findByID(id);
    }

    public boolean deleteDoctorByID(int id) {
        return repo.delete(id);
    }

    public boolean updateDoctor(Doctor doc) {
        return repo.updateObj(doc);
    }

    public boolean updateDoctorById(int id, Doctor doc) {
        return repo.update(id, doc);
    }

    public List<Doctor> getAllDoctors(int page, int size) {
        return repo.findAllPagination(page, size);
    }

    public int countDoctors() {
        return repo.count();
    }
}
