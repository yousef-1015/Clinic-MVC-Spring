package com.example.clinicmvcspring.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorDeletedEvent {
    private final String doctorName;
    private final String message;

}
