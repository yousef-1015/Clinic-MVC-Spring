package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.Patient;

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

    // private final RowMapper<Patient> rowMapper = (res, rowNum) -> {
    // Patient pat = new Patient();
    // pat.setId(res.getInt("id"));
    // pat.setFirstName(res.getString("first_name"));
    // pat.setLastName(res.getString("last_name"));
    // pat.setEmail(res.getString("email"));
    // pat.setCreatedAt(res.getTimestamp("created_at"));
    // return pat;
    // };
    private final RowMapper<Patient> rowMapper = new BeanPropertyRowMapper<>(Patient.class);

    public int insert(Patient newPatient) {
        String sql = "INSERT INTO patients (first_name, last_name, email) " +
                "VALUES (:firstName, :lastName, :email)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", newPatient.getFirstName())
                .addValue("lastName", newPatient.getLastName())
                .addValue("email", newPatient.getEmail());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().intValue();

    }

    public List<Patient> getAll() {
        String sql = "SELECT * FROM patients";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Patient> findAllPagination(int page, int size) {
        String sql = "SELECT * FROM patients LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, rowMapper, size, offset);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM patients";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Patient getByID(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int delete(Patient pat) {
        String sql = "DELETE FROM patients WHERE id = ?";
        jdbcTemplate.update(sql, pat.getId());
        return pat.getId();
    }

    public int delete(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    public int update(int id, Patient pat) {
        String sql = "UPDATE patients SET first_name = :firstName, last_name = :lastName, " +
                "email = :email WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", pat.getFirstName())
                .addValue("lastName", pat.getLastName())
                .addValue("email", pat.getEmail())
                .addValue("id", id);

        namedJdbcTemplate.update(sql, params);
        return id;
    }

}
