package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.PatientModel;

@Repository
public class PatientRepo {
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public PatientRepo(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private final RowMapper<PatientModel> rowMapper = (res, rowNum) -> {
        PatientModel pat = new PatientModel();
        pat.setId(res.getInt("id"));
        pat.setFirstName(res.getString("first_name"));
        pat.setLastName(res.getString("last_name"));
        pat.setEmail(res.getString("email"));
        pat.setCreatedAt(res.getTimestamp("created_at"));
        return pat;
    };

    public boolean insertNewPatient(PatientModel newPatient) {
        String sql = "INSERT INTO patients (first_name, last_name, email) " +
                "VALUES (:firstName, :lastName, :email)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", newPatient.getFirstName())
                .addValue("lastName", newPatient.getLastName())
                .addValue("email", newPatient.getEmail());
        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public List<PatientModel> getAllPatients() {
        String sql = "SELECT * FROM patients";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public PatientModel getPatientByID(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean deletePatientFromDB(PatientModel pat) {
        String sql = "DELETE FROM patients WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, pat.getId());
        return rowsAffected > 0;
    }

    public boolean deletePatientFromDB(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean updatePatient(int id, PatientModel pat) {
        String sql = "UPDATE patients SET first_name = :firstName, last_name = :lastName, " +
                "email = :email WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", pat.getFirstName())
                .addValue("lastName", pat.getLastName())
                .addValue("email", pat.getEmail())
                .addValue("id", id);

        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

}
