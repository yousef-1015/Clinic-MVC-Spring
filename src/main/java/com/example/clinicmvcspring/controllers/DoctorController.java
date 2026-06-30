package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.DoctorModel;
import com.example.clinicmvcspring.services.DoctorService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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
    public Map<String, Object> deleteDoctor(@PathVariable int id) {
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

    @PutMapping("/{id}")
    public Map<String, Object> updateDoctor(@PathVariable int id, @RequestBody DoctorModel doc) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {

            DoctorModel existingDoc = doctorService.getDoctorByID(id);

            if (existingDoc == null) {
                response.put("message", "ERROR: Doctor not found");
                response.put("idRequested", id);
                return response;
            }
            doctorService.updateDoctorById(id, doc);
            response.put("message", "Doctor Updated Successfully !!!");
            response.put("firstName", doc.getFirstName());
            response.put("lastName", doc.getLastName());
            response.put("email", doc.getEmail());
            response.put("salary", doc.getSalary());
            response.put("specialty", doc.getSpecialty());
            response.put("hire date", doc.getHireDate());
            response.put("hire date", existingDoc.getHireDate());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating doctor");
            response.put("Reason", e.getMessage());

            return response;

        }

    }

    @PatchMapping("/{id}")
    public Map<String, Object> partialDocUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {

            DoctorModel existingDoc = doctorService.getDoctorByID(id);

            if (existingDoc == null) {
                response.put("message", "ERROR: Doctor not found");
                response.put("idRequested", id);
                return response;
            }
            if (toUpdate.containsKey("firstName")) {
                existingDoc.setFirstName((String) toUpdate.get("firstName"));
            }
            if (toUpdate.containsKey("lastName")) {
                existingDoc.setLastName((String) toUpdate.get("lastName"));
            }
            if (toUpdate.containsKey("email")) {
                existingDoc.setEmail((String) toUpdate.get("email"));
            }
            if (toUpdate.containsKey("specialty")) {
                existingDoc.setSpecialty((String) toUpdate.get("specialty"));
            }
            if (toUpdate.containsKey("salary")) {
                Number salaryVal = (Number) toUpdate.get("salary");
                existingDoc.setSalary(salaryVal.doubleValue());
            }

            doctorService.updateDoctorById(id, existingDoc);
            response.put("message", "Doctor Updated Successfully !!!");

            response.put("lastName", existingDoc.getLastName());
            response.put("email", existingDoc.getEmail());
            response.put("salary", existingDoc.getSalary());
            response.put("specialty", existingDoc.getSpecialty());
            response.put("hire date", existingDoc.getHireDate());
            response.put("hire date", existingDoc.getHireDate());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating doctor");
            response.put("Reason", e.getMessage());

            return response;

        }
    }

}
