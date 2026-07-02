package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.Medication;

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

    RowMapper<Medication> rowMapper = (res, rowNum) -> {
        Medication med = new Medication();
        med.setId(res.getInt("id"));
        med.setMedicationName(res.getString("medication_name"));
        med.setCreatedAt(res.getTimestamp("created_at"));
        return med;

    };

    public int insert(Medication med) {
        String sql = "INSERT INTO medications (medication_name) VALUES (:medicationName)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("medicationName", med.getMedicationName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, params, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public List<Medication> findAll() {
        String sql = "SELECT * FROM medications";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Medication findByID(int id) {
        String sql = "SELECT * FROM medications WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Medication> findAllPagination(int page, int size) {
        String sql = "SELECT * FROM medications LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, rowMapper, size, offset);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM medications";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int delete(Medication med) {
        String sql = "DELETE FROM medications WHERE id = ?";
        jdbcTemplate.update(sql, med.getId());

        return med.getId();
    }

    public int delete(int id) {
        String sql = "DELETE FROM medications WHERE id = ?";
         jdbcTemplate.update(sql, id);

        return id;

    }

    public int update(int id, Medication med) {
        String sql = "UPDATE medications SET medication_name = :medicationName WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("medicationName", med.getMedicationName())
                .addValue("id", id);

        namedJdbcTemplate.update(sql, params);
        return id;
    }

}
