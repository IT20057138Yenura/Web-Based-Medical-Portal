package com.medicalportal.repository;

import com.medicalportal.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPatientId(String patientId);

    boolean existsByEmail(String email);

    boolean existsByPatientId(String patientId);
}
