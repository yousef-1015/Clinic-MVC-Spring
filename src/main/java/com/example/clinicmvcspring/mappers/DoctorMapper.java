package com.example.clinicmvcspring.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.clinicmvcspring.dtos.DoctorDTO;
import com.example.clinicmvcspring.models.Doctor;

@Mapper(componentModel = "spring") 
public interface DoctorMapper {

    // Entity to DTO (MapStruct matches same-name fields automatically)
    DoctorDTO doctorToDoctorDTO(Doctor doctor);

    // 2. DTO to Entity (We ignore salary and hireDate they are not in the DTO)
    @Mapping(target = "salary", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    Doctor doctorDTOToDoctor(DoctorDTO doctorDTO);
}
