
package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;

public class PrescriptionDTO {
    private Integer id;
    private String prescriptionNotes;
    private Timestamp createdAt;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(Integer id, String prescriptionNotes, Timestamp createdAt) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrescriptionNotes() {
        return prescriptionNotes;
    }

    public void setPrescriptionNotes(String prescriptionNotes) {
        this.prescriptionNotes = prescriptionNotes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
