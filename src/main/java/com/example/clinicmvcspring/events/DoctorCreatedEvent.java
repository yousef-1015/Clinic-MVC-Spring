package com.example.clinicmvcspring.events;

import lombok.Getter;

@Getter
public class DoctorCreatedEvent {
    private final String doctorName;
    private final String doctorEmail;

    public DoctorCreatedEvent(String doctorName, String doctorEmail) {
        this.doctorEmail = doctorEmail;
        this.doctorName = doctorName;
    }

}
