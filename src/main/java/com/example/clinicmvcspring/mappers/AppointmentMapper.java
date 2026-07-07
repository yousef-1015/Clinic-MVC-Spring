package com.example.clinicmvcspring.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.models.Appointment;

@Mapper(componentModel = "spring", uses = { PrescriptionMapper.class })
public interface AppointmentMapper {

    // source = name from object
    // target = name from dto
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    AppointmentDTO appointmentToAppointmentDTO(Appointment appointment);

    // Map DTO back to Entity
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    Appointment appointmentDTOToAppointment(AppointmentDTO appointmentDTO);
}