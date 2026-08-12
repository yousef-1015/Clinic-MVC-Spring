package com.example.clinicmvcspring.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.dtos.DoctorDTO;
import com.example.clinicmvcspring.mappers.DoctorMapper;
import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;
import com.example.clinicmvcspring.messaging.producers.DoctorEventProducer;
import com.example.clinicmvcspring.messaging.producers.KafkaDoctorEventProducer;
import com.example.clinicmvcspring.models.Doctor;
import com.example.clinicmvcspring.repositories.DoctorRepo;
import com.example.clinicmvcspring.specifications.DoctorSpecifications;

@Service
public class DoctorService {

    private final DoctorRepo repo;
    private final DoctorMapper mapper;
    // private final ApplicationEventPublisher applicationEventPublisher;
    private final DoctorEventProducer doctorEventProducer;
    private final KafkaDoctorEventProducer kafkaDoctorEventProducer;

    public DoctorService(DoctorRepo repo, DoctorMapper mapper, DoctorEventProducer doctorEventProducer,
            KafkaDoctorEventProducer kafkaDoctorEventProducer) {
        this.repo = repo;
        this.mapper = mapper;
        this.doctorEventProducer = doctorEventProducer;
        this.kafkaDoctorEventProducer = kafkaDoctorEventProducer;
        // this.applicationEventPublisher = applicationEventPublisher;
    }

    public Doctor addDoctor(Doctor newDoctor) {
        Doctor savedDoc = repo.save(newDoctor);// using the .save from the built in methods in JpaRepositories
        // publish the event
        publishDoctorCreatedEvent(savedDoc);
        return savedDoc;
    }

    public List<Doctor> getAllDoctors() {
        return repo.findAll();// the job lies on the repo to access the DB
    }

    @CacheEvict(value = "Doctors", key = "#docToDelete.getId()")
    public void deleteDoctor(Doctor docToDelete) {
        publishDoctorDeletedEvent(docToDelete);

        repo.delete(docToDelete);
    }

    @Cacheable(value = "Doctors", key = "#id") // pull data on miss
    public Optional<Doctor> getDoctorByID(int id) {
        return repo.findById(id);
    }

    @CacheEvict(value = "Doctors", key = "#id") // remove deleted data from cache
    public void deleteDoctorByID(int id) {
        Doctor docToDelete = getDoctorByID(id)
                .orElseThrow(() -> new NoSuchElementException("No Doctor found with the ID: " + id));
        publishDoctorDeletedEvent(docToDelete);
        repo.deleteById(id);
    }

    @CachePut(value = "Doctors", key = "#doc.getId()") // push updates from db to cache
    public Doctor updateDoctor(Doctor doc) {
        Doctor updatedDoctor = repo.save(doc);
        publishDoctorUpdatedEvent(updatedDoctor);
        return updatedDoctor;
    }

    @CachePut(value = "Doctors", key = "#id")
    public Doctor updateDoctorById(int id, Doctor doc) {
        doc.setId(id);
        Doctor updatedDoctor = repo.save(doc);
        publishDoctorUpdatedEvent(updatedDoctor);
        return updatedDoctor;
    }

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctorPage = repo.findAll(pageable);
        return doctorPage.map(doc -> mapper.doctorToDoctorDTO(doc));
    }

    public Page<Doctor> findDoctorsBySpecialty(String specialty, Pageable pageable) {
        return repo.findBySpecialty(specialty, pageable);
    }

    public Page<Doctor> findDoctorsBySpecialtyAndSalary(String specialty, BigDecimal salary, Pageable pageable) {

        // Combine specifications
        Specification<Doctor> spec = Specification
                .where(DoctorSpecifications.hasSpecialty(specialty))
                .and(DoctorSpecifications.salaryGreaterThan(salary));

        return repo.findAll(spec, pageable);
    }

    // publish event
    private void publishDoctorCreatedEvent(Doctor doc) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        DoctorCreatedMessage event = new DoctorCreatedMessage(doc.getFirstName() + " " + doc.getLastName(),
                doc.getEmail(),
                currentUsername, now, doc.getId());
        doctorEventProducer.sendDoctorCreated(event);
        // KAFKA EVENT PUBLISH
        kafkaDoctorEventProducer.sendDoctorCreated(event);

    }

    private void publishDoctorUpdatedEvent(Doctor doc) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        DoctorUpdatedMessage event = new DoctorUpdatedMessage(doc.getFirstName() + " " + doc.getLastName(),
                "Updated Successfully",
                currentUsername, now, doc.getId());
        doctorEventProducer.sendDoctorUpdated(event);
        // KAFKA EVENT PUBLISH
        kafkaDoctorEventProducer.sendDoctorUpdated(event);

    }

    private void publishDoctorDeletedEvent(Doctor doc) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        DoctorDeletedMessage event = new DoctorDeletedMessage(doc.getFirstName() + " " + doc.getLastName(),
                "Deleted Successfully",
                currentUsername, now, doc.getId());
        // KAFKA EVENT PUBLISH
        kafkaDoctorEventProducer.sendDoctorDeleted(event);

        doctorEventProducer.sendDoctorDeleted(event);

    }

}