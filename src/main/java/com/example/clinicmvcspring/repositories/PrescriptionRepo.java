package com.example.clinicmvcspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.example.clinicmvcspring.models.Prescription;

public interface PrescriptionRepo extends JpaRepository<Prescription, Integer> {
    @Query(value = "SELECT pm.medication_id as medicationId, m.medication_name as medicationName, pm.dosage as dosage, pm.frequency as frequency "
            +
            "FROM prescription_medications pm " +
            "JOIN medications m ON pm.medication_id = m.id " +
            "WHERE pm.prescription_id = :prescriptionId", nativeQuery = true)
    // prescriptionId like the jdbcNamedParameter se we use th @Param to map from
    // sql query to variable
    List<PrescriptionMedicationProjection> getMedicationsForPrescription(@Param("prescriptionId") int prescriptionId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO prescription_medications (prescription_id, medication_id, dosage, frequency) " +
            "VALUES (:prescriptionId, :medicationId, :dosage, :frequency)", nativeQuery = true)
    void addMedicationToPrescription(@Param("prescriptionId") int prescriptionId,
            @Param("medicationId") int medicationId,
            @Param("dosage") String dosage,
            @Param("frequency") String frequency);
}
