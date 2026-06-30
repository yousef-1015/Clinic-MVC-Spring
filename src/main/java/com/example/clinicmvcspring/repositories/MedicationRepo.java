package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.MedicationModel;

@Repository
public class MedicationRepo {
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MedicationRepo(DataSource dataSource, JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    RowMapper<MedicationModel> rowMapper = (res, rowNum) -> {
        MedicationModel med = new MedicationModel();
        med.setId(res.getInt("id"));
        med.setMedicationName(res.getString("medication_name"));
        med.setCreatedAt(res.getTimestamp("created_at"));
        return med;

    };

    public boolean insertNewMedication(MedicationModel med) {
        String sql = "INSERT INTO medications (medication_name) VALUES (:medicationName)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("medicationName", med.getMedicationName());
        int rowsAffected = namedJdbcTemplate.update(sql, params);

        return rowsAffected > 0 ? true : false;

    }

    public List<MedicationModel> findAllMedications() {
        String sql = "SELECT * FROM medications";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public MedicationModel getMedicationByID(int id) {
        String sql = "SELECT * FROM medications WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            System.out.print(e.getMessage());
            return null;
        }
    }

    public boolean deleteMedicationFromDB(MedicationModel med) {
        String sql = "DELETE FROM medications WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, med.getId());

        return rowsAffected > 0;
    }

    public boolean deleteMedicationFromDB(int id) {
        String sql = "DELETE FROM medications WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        return rowsAffected > 0;

    }

}
