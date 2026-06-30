package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.PrescriptionModel;
import com.example.clinicmvcspring.services.PrescriptionService;

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
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<PrescriptionModel> getPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    public Object getPrescriptionByID(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            PrescriptionModel pres = prescriptionService.getPrescriptionByID(id);
            if (pres == null) {
                response.put("message", "ERROR: Prescription not found");
                response.put("idRequested", id);
                return response;
            }
            return pres;
        } catch (Exception e) {
            response.put("message", "ERROR searching for prescription");
            response.put("reason", e.getMessage());
            return response;
        }
    }

    @PostMapping
    public Map<String, Object> addNewPrescription(@RequestBody PrescriptionModel newPres) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            prescriptionService.addPrescription(newPres);
            response.put("message", "Prescription added successfully !!!");
            response.put("prescriptionNotes", newPres.getPrescriptionNotes());
            response.put("appointmentId", newPres.getAppointmentId());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR adding prescription");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deletePrescription(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            PrescriptionModel pres = prescriptionService.getPrescriptionByID(id);

            if (pres == null) {
                response.put("message", "ERROR: Prescription not found");
                response.put("idRequested", id);
                return response;
            }
            boolean isDeleted = prescriptionService.deletePrescriptionByID(id);

            if (isDeleted == false) {
                response.put("message", "ERROR: Prescription was'nt Deleted");
                response.put("idRequested", id);
                return response;
            }
            response.put("message", "Prescription Deleted Successfully !!!!");
            response.put("idRequested", id);
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR DELETING prescription");
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                response.put("reason", "This prescription contains medications and cannot be deleted");
            } else {
                response.put("reason", e.getMessage());
            }
            return response;
        }
    }

    @PutMapping("/{id}")
    public Map<String, Object> updatePrescription(@PathVariable int id, @RequestBody PrescriptionModel pres) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            PrescriptionModel existingPres = prescriptionService.getPrescriptionByID(id);

            if (existingPres == null) {
                response.put("message", "ERROR: Prescription not found");
                response.put("idRequested", id);
                return response;
            }

            prescriptionService.updatePrescriptionById(id, pres);

            response.put("message", "Prescription Updated Successfully !!!");
            response.put("prescriptionNotes", pres.getPrescriptionNotes());
            response.put("appointmentId", pres.getAppointmentId());
            response.put("created date", existingPres.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating prescription");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @PatchMapping("/{id}")
    public Map<String, Object> partialPrescriptionUpdate(@PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            PrescriptionModel existingPres = prescriptionService.getPrescriptionByID(id);

            if (existingPres == null) {
                response.put("message", "ERROR: Prescription not found");
                response.put("idRequested", id);
                return response;
            }
            if (toUpdate.containsKey("prescriptionNotes")) {
                existingPres.setPrescriptionNotes((String) toUpdate.get("prescriptionNotes"));
            }
            if (toUpdate.containsKey("appointmentId")) {
                existingPres.setAppointmentId(((Number) toUpdate.get("appointmentId")).intValue());
            }

            prescriptionService.updatePrescriptionById(id, existingPres);

            response.put("message", "Prescription Updated Successfully !!!");
            response.put("prescriptionNotes", existingPres.getPrescriptionNotes());
            response.put("appointmentId", existingPres.getAppointmentId());
            response.put("created date", existingPres.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating prescription");
            response.put("Reason", e.getMessage());
            return response;
        }
    }
}
