package com.example.clinicmvcspring.specifications;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.clinicmvcspring.models.Doctor;

public class DoctorSpecifications {

    public static Specification<Doctor> hasSpecialty(String specialty) {
        // root: the base entity
        // query: CriteriaQuery, is the blueprint (the actual query)
        // cb CriteriaBuilder: has methods and tools to comapre
        return (root, query, cb) -> {
            if (specialty == null || specialty.trim().isEmpty()) {
                return cb.conjunction(); // empty condition
                // we cant return null so we return an always true condition
                // like SELECT * FROM doctors WHERE 1=1
            }
            return cb.equal(root.get("specialty"), specialty);
        };

    }

    public static Specification<Doctor> salaryGreaterThan(BigDecimal salary) {
        return (root, query, cb) -> {
            if (salary == null) {
                return cb.conjunction(); // empty condition
            }
            return cb.greaterThan(root.get("salary"), salary);

        };

    }

}
