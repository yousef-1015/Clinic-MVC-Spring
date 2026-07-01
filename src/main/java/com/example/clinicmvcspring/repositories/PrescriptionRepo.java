package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.dtos.PrescriptionMedicationDTO;
import com.example.clinicmvcspring.models.Prescription;

@Repository
public class PrescriptionRepo {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public PrescriptionRepo(DataSource dataSource, JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private final RowMapper<Prescription> rowMapper = (res, rowNum) -> new Prescription(
            res.getInt("id"),
            res.getString("prescription_notes"),
            res.getInt("appointment_id"),
            res.getTimestamp("created_at"));

    public boolean insert(Prescription pres) {
        String sql = "INSERT INTO prescriptions (prescription_notes, appointment_id) " +
                "VALUES (:prescriptionNotes, :appointmentId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("prescriptionNotes", pres.getPrescriptionNotes())
                .addValue("appointmentId", pres.getAppointmentId());
        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public List<Prescription> findAll() {
        String sql = "SELECT * FROM prescriptions";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Prescription getByID(int id) {
        String sql = "SELECT * FROM prescriptions WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean delete(Prescription pres) {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, pres.getId());
        return rowsAffected > 0;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean update(int id, Prescription pres) {
        String sql = "UPDATE prescriptions SET prescription_notes = :prescriptionNotes, " +
                "appointment_id = :appointmentId WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("prescriptionNotes", pres.getPrescriptionNotes())
                .addValue("appointmentId", pres.getAppointmentId())
                .addValue("id", id);

        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public List<PrescriptionMedicationDTO> getMedicationsForPrescription(int prescriptionId) {
        String sql = "SELECT pm.medication_id, m.medication_name, pm.dosage, pm.frequency " +
                "FROM prescription_medications pm " +
                "JOIN medications m ON pm.medication_id = m.id " +
                "WHERE pm.prescription_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PrescriptionMedicationDTO(
                rs.getInt("medication_id"),
                rs.getString("medication_name"),
                rs.getString("dosage"),
                rs.getString("frequency")), prescriptionId);
    }

}
