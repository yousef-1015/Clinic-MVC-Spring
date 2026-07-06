package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.dtos.PrescriptionDetailDTO;
import com.example.clinicmvcspring.dtos.PrescriptionMedicationDTO;
import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PrescriptionService {

    private final PrescriptionRepo repo;

    public PrescriptionService(PrescriptionRepo repo) {
        this.repo = repo;
    }

    public Prescription addPrescription(Prescription pres) {
        return repo.save(pres);
    }

    public List<Prescription> getAllPrescriptions() {
        return repo.findAll();
    }

    public Optional<Prescription> getPrescriptionByID(int id) {
        return repo.findById(id);
    }

    public Optional<PrescriptionDetailDTO> getPrescriptionDetailsByID(int id) {
        Optional<Prescription> presOpt = repo.findById(id);

        if (presOpt.isEmpty()) {
            return Optional.empty();
        }

        Prescription pres = presOpt.get();

        List<PrescriptionMedicationDTO> meds = getMedicationsForPrescription(id);

        PrescriptionDetailDTO details = new PrescriptionDetailDTO(
                pres.getId(),
                pres.getPrescriptionNotes(),
                pres.getAppointmentId(),
                pres.getCreatedAt(),
                meds);

        return Optional.of(details);
    }

    public void deletePrescription(Prescription pres) {
        repo.delete(pres);
    }

    public void deletePrescriptionByID(int id) {
        repo.deleteById(id);
    }

    public Prescription updatePrescriptionById(int id, Prescription pres) {
        pres.setId(id);
        return repo.save(pres);
    }

    public List<PrescriptionMedicationDTO> getMedicationsForPrescription(int prescriptionId) {
        return repo.getMedicationsForPrescription(prescriptionId).stream() // map from the projection tto the dto
                .map(p -> new PrescriptionMedicationDTO(
                        p.getMedicationId(),
                        p.getMedicationName(),
                        p.getDosage(),
                        p.getFrequency()))
                .toList();
    }

    public List<Prescription> getAllPrescriptionsPaginated(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public long count() {
        return repo.count();
    }

    public PrescriptionDetailDTO addPrescriptionWithMedications(PrescriptionDetailDTO inputDTO) {
        Prescription pres = new Prescription(inputDTO.getPrescriptionNotes(), inputDTO.getAppointmentId());
        Prescription savedPres = repo.save(pres);

        if (inputDTO.getMedications() != null) {
            // add each med to the pres
            for (PrescriptionMedicationDTO med : inputDTO.getMedications()) {
                repo.addMedicationToPrescription(
                        savedPres.getId(),
                        med.getMedicationId(),
                        med.getDosage(),
                        med.getFrequency());
            }
        }

        inputDTO.setId(savedPres.getId());
        inputDTO.setCreatedAt(savedPres.getCreatedAt());
        return inputDTO;
    }
}