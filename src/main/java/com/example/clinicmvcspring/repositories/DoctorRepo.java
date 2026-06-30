package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.DoctorModel;

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

    public boolean insertANewDoctor(DoctorModel doc) {
        String sql = "INSERT INTO doctors (first_name, last_name, email, salary, specialty) " +
                "VALUES (:firstName, :lastName, :email, :salary, :specialty)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty());

        int rowsAffected = namedJdbcTemplate.update(sql, params);
        // execute query, map the args, return num of rows changes

        return rowsAffected > 0;
    }

    public List<DoctorModel> findAllDoctors() {
        String sql = "SELECT * FROM doctors";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new DoctorModel(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getDouble("salary"),
                rs.getDate("hire_date"),
                rs.getString("specialty")));
    }

    public boolean deleteDoctorFromDB(DoctorModel docToDelete) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, docToDelete.getId());
        return rowsAffected > 0;
    }

    public boolean deleteDoctorFromDB(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public DoctorModel getDoctorByID(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new DoctorModel(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getDouble("salary"),
                    rs.getDate("hire_date"),
                    rs.getString("specialty")), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean updateDoctorInDB(DoctorModel doc) {
        String sql = "UPDATE doctors SET first_name = :firstName, last_name = :lastName, " +
                "email = :email, salary = :salary, specialty = :specialty WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty())
                .addValue("id", doc.getId());

        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

        public boolean updateDoctor(int id,DoctorModel doc) {
        String sql = "UPDATE doctors SET first_name = :firstName, last_name = :lastName, " +
                "email = :email, salary = :salary, specialty = :specialty WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", doc.getFirstName())
                .addValue("lastName", doc.getLastName())
                .addValue("email", doc.getEmail())
                .addValue("salary", doc.getSalary())
                .addValue("specialty", doc.getSpecialty())
                .addValue("id", id);

        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

}