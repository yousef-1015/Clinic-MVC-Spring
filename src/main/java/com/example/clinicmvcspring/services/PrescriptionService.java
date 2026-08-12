package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.dtos.PrescriptionDetailDTO;
import com.example.clinicmvcspring.dtos.PrescriptionMedicationDTO;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.Prescription;
import com.example.clinicmvcspring.repositories.PrescriptionRepo;

@Service
public class PrescriptionService {

    private final PrescriptionRepo repo;

    public PrescriptionService(PrescriptionRepo repo) {
        this.repo = repo;

    }

    @Audit(action = AuditAction.CREATE)
    public Prescription addPrescription(Prescription pres) {
        return repo.save(pres);
    }

    public List<PrescriptionDetailDTO> getAllPrescriptions() {
        return repo.findAll().stream()
                .map(this::convertToDetailDTO)
                .toList();
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
                pres.getAppointment().getId(),
                pres.getCreatedAt(),
                meds);

        return Optional.of(details);
    }

    @Audit(action = AuditAction.DELETE)
    public void deletePrescription(Prescription pres) {
        repo.delete(pres);
    }

    @Audit(action = AuditAction.DELETE)
    public void deletePrescriptionByID(int id) {
        repo.deleteById(id);
    }

    @Audit(action = AuditAction.UPDATE)
    public PrescriptionDetailDTO updatePrescriptionById(int id, Prescription pres) {
        pres.setId(id);
        return convertToDetailDTO(repo.save(pres));
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

    public Page<PrescriptionDetailDTO> getAllPrescriptionsPaginated(Pageable pageable) {
        Page<Prescription> prePage = repo.findAll(pageable);
        return prePage.map(pre -> convertToDetailDTO(pre));
    }

    @Audit(action = AuditAction.CREATE)
    @Transactional
    public PrescriptionDetailDTO addPrescriptionWithMedications(PrescriptionDetailDTO inputDTO) {
        Appointment appointment = new Appointment();
        appointment.setId(inputDTO.getAppointmentId());
        Prescription pres = new Prescription(inputDTO.getPrescriptionNotes(), appointment);

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

    public PrescriptionDetailDTO convertToDetailDTO(Prescription pres) {
        List<PrescriptionMedicationDTO> meds = getMedicationsForPrescription(pres.getId());
        return new PrescriptionDetailDTO(
                pres.getId(),
                pres.getPrescriptionNotes(),
                pres.getAppointment() != null ? pres.getAppointment().getId() : null,
                pres.getCreatedAt(),
                meds);
    }

}