package com.example.clinicmvcspring.repositories;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.Doctor;

@Repository
public class DoctorRepo {
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public DoctorRepo(DataSource dataSource, JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    // private final RowMapper<Doctor> rowMapper = (rs, rowNum) -> new
    // Doctor(rs.getInt("id"),
    // rs.getString("first_name"),
    // rs.getString("last_name"),
    // rs.getString("email"),
    // rs.getDouble("salary"),
    // rs.getDate("hire_date"),
    // rs.getString("specialty"));
    private final RowMapper<Doctor> rowMapper = new BeanPropertyRowMapper<>(Doctor.class);

    public int insert(Doctor doc) {
        String sql = "INSERT INTO doctors (first_name, last_name, email, salary, specialty) " +
                "VALUES (:firstName, :lastName, :email, :salary, :specialty)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty());

        KeyHolder keyHolder = new GeneratedKeyHolder(); // from spring to hold ID generated fro DB
        namedJdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().intValue(); // GETS THE PRIMARY KEY
    }

    public List<Doctor> findAll() {
        String sql = "SELECT * FROM doctors ORDER BY id";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Doctor> findAllPagination(int page, int size) {
        String sql = "SELECT * FROM doctors ORDER BY id LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, rowMapper, size, offset);

    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM doctors";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int delete(Doctor docToDelete) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        jdbcTemplate.update(sql, docToDelete.getId());
        return docToDelete.getId();
    }

    public int delete(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    public Optional<Doctor> findByID(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int update(Doctor doc) {
        String sql = "UPDATE doctors SET first_name = :firstName, last_name = :lastName, " +
                "email = :email, salary = :salary, specialty = :specialty WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty())
                .addValue("id", doc.getId());

        namedJdbcTemplate.update(sql, params);
        return doc.getId();
    }

    public int update(int id, Doctor doc) {
        String sql = "UPDATE doctors SET first_name = :firstName, last_name = :lastName, " +
                "email = :email, salary = :salary, specialty = :specialty WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty())
                .addValue("id", id);

        namedJdbcTemplate.update(sql, params);
        return id;
    }

}