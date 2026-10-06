package com.medicalportal.repository;

import com.medicalportal.model.Appointment;
import com.medicalportal.model.AppointmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Optional<Appointment> findByAppointmentId(String appointmentId);

    Optional<Appointment> findByAppointmentIdAndPatientId(String appointmentId, String patientId);

    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(String patientId);

    List<Appointment> findByPatientIdAndStatusOrderByAppointmentDateDescAppointmentTimeDesc(String patientId, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(String doctorId, LocalDate appointmentDate, AppointmentStatus status);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatus(String doctorId, LocalDate appointmentDate, String appointmentTime, AppointmentStatus status);

    boolean existsByPatientIdAndAppointmentDateAndAppointmentTimeAndStatus(String patientId, LocalDate appointmentDate, String appointmentTime, AppointmentStatus status);

    void deleteByPatientId(String patientId);
}
