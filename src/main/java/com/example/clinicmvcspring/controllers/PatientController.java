package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.PatientModel;
import com.example.clinicmvcspring.services.PatientService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    public List<PatientModel> getAllPatients() {
        return patientService.getAllPatients();

    }

    @GetMapping("/{id}")
    public Object getPatientByID(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            PatientModel pat = patientService.getPatientByID(id);
            if (pat == null) {
                response.put("message", "ERROR: Patient not found");
                response.put("idRequested", id);
                return response;
            }
            return pat;
        } catch (Exception e) {
            response.put("message", "ERROR searching for patient");
            response.put("reason", e.getMessage());
            return response;
        }
    }

    @PostMapping
    public Map<String, Object> addNewPatient(@RequestBody PatientModel newPatient) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            patientService.addPatient(newPatient);
            response.put("message", "Patient added successfully !!!");
            response.put("firstName", newPatient.getFirstName());
            response.put("lastName", newPatient.getLastName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR adding patient");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deletePatient(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            PatientModel pat = patientService.getPatientByID(id);
            if (pat == null) {
                response.put("message", "ERROR: Patient not found");
                response.put("idRequested", id);
                return response;
            }
            boolean isDeleted = patientService.deletePatientByID(id);
            if (isDeleted == false) {
                response.put("message", "ERROR: Patient was'nt Deleted");
                response.put("idRequested", id);
                return response;
            }
            response.put("message", "Patient Deleted Successfully !!!!");
            response.put("idRequested", id);
            response.put("deleted patient name", pat.getFirstName() + " " + pat.getLastName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR DELETING patient");
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                response.put("reason", "This patient has active appointments and cannot be deleted");
            } else {
                response.put("reason", e.getMessage());
            }
            return response;
        }
    }

    @PutMapping("/{id}")
    public Map<String, Object> updatePatient(@PathVariable int id, @RequestBody PatientModel pat) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            PatientModel existingPat = patientService.getPatientByID(id);
            if (existingPat == null) {
                response.put("message", "ERROR: Patient not found");
                response.put("idRequested", id);
                return response;
            }
            patientService.updatePatientById(id, pat);
            response.put("message", "Patient Updated Successfully !!!");
            response.put("firstName", pat.getFirstName());
            response.put("lastName", pat.getLastName());
            response.put("email", pat.getEmail());
            response.put("created date", existingPat.getCreatedAt());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating patient");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @PatchMapping("/{id}")
    public Map<String, Object> partialPatientUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            PatientModel existingPat = patientService.getPatientByID(id);
            if (existingPat == null) {
                response.put("message", "ERROR: Patient not found");
                response.put("idRequested", id);
                return response;
            }
            if (toUpdate.containsKey("firstName")) {
                existingPat.setFirstName((String) toUpdate.get("firstName"));
            }
            if (toUpdate.containsKey("lastName")) {
                existingPat.setLastName((String) toUpdate.get("lastName"));
            }
            if (toUpdate.containsKey("email")) {
                existingPat.setEmail((String) toUpdate.get("email"));
            }
            patientService.updatePatientById(id, existingPat);
            response.put("message", "Patient Updated Successfully !!!");
            response.put("lastName", existingPat.getLastName());
            response.put("email", existingPat.getEmail());
            response.put("created date", existingPat.getCreatedAt());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating patient");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

}
