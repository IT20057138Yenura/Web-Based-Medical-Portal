package com.medicalportal.repository;

import com.medicalportal.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findByDoctorId(String doctorId);

    List<Doctor> findByStatus(String status);

    List<Doctor> findBySpecializationIgnoreCaseAndStatus(String specialization, String status);

    List<Doctor> findByDoctorNameContainingIgnoreCaseAndStatus(String doctorName, String status);

    boolean existsByDoctorId(String doctorId);
}
