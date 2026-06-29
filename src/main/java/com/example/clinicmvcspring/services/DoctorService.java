package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class DoctorService {

    private DoctorRepo repo;

  public DoctorService(DoctorRepo repo) {
        this.repo = repo;
    }

    public boolean addDoctor(DoctorModel newDoctor) {
        return repo.insertANewDoctor(newDoctor);// the job lies on the repo to access the DB
    }

    public List<DoctorModel> getAllDoctors() {
        return repo.findAllDoctors();// the job lies on the repo to access the DB
    }

    public boolean deleteDoctor(DoctorModel docToDelete) {
        return repo.deleteDoctorFromDB(docToDelete);
    }

    public DoctorModel getDoctorByID(int id) {
        return repo.getDoctorByID(id);
    }

    public boolean deleteDoctorByID(int id) {
        return repo.deleteDoctorFromDB(id);
    }

    public boolean updateDoctor(DoctorModel doc) {
        return repo.updateDoctorInDB(doc);
    }

}
