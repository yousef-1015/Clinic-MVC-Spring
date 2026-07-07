package com.example.clinicmvcspring.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinicmvcspring.dtos.PrescriptionDTO;
import com.example.clinicmvcspring.models.Prescription;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    PrescriptionDTO PrescriptionToPrescriptionDTO(Prescription prescription);

    // Map DTO back to Entity
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "appointmentId", ignore = true)
    Prescription prescriptionDTOToPrescription(PrescriptionDTO prescriptionDTO);
}
