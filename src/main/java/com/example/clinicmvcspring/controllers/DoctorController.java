package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.DoctorModel;
import com.example.clinicmvcspring.services.DoctorService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get all doctors as JSON
    @GetMapping
    public List<DoctorModel> getDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Object getDoctorByID(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        DoctorModel doc = doctorService.getDoctorByID(id);
        try {
            if (doc == null) {
                response.put("message", "ERROR: Doctor not found");
                response.put("idRequested", id);
                return response;
            }
            return doc;
        } catch (Exception e) {
            response.put("message", "ERROR searching for doctor");
            response.put("reason", e.getMessage());
            return response;
        }

    }

    @PostMapping
    public Map<String, Object> addNewDoctor(@RequestBody DoctorModel newDoc) {
        // json res
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            doctorService.addDoctor(newDoc);
            response.put("message", "Doctor added successfully !!!");
            response.put("firstName", newDoc.getFirstName());
            response.put("lastName", newDoc.getLastName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR adding doctor");
            response.put("Reason", e.getMessage());

            return response;

        }

    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteDroctor(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            DoctorModel doc = doctorService.getDoctorByID(id);

            if (doc == null) {
                response.put("message", "ERROR: Doctor not found");
                response.put("idRequested", id);
                return response;
            }
            boolean isDeleted = doctorService.deleteDoctorByID(id);

            if (isDeleted == false) {
                response.put("message", "ERROR: Doctor was'nt Deleted");
                response.put("idRequested", id);
                return response;
            }
            response.put("message", "Doctor Deleted Successfully !!!!");
            response.put("idRequested", id);
            response.put("deleted doctor name", doc.getFirstName() + " " + doc.getLastName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR DELETING doctor");
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                response.put("reason", "This doctor has active appointments and cannot be deleted");
            } else {
                response.put("reason", e.getMessage());
            }
            return response;
        }

    }

}
