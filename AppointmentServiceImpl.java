package com.medicalportal.service.impl;

import com.medicalportal.dto.request.AppointmentCreateRequest;
import com.medicalportal.dto.request.AppointmentRescheduleRequest;
import com.medicalportal.dto.response.AppointmentResponse;
import com.medicalportal.exception.InvalidOperationException;
import com.medicalportal.exception.ResourceNotFoundException;
import com.medicalportal.exception.SlotUnavailableException;
import com.medicalportal.model.Appointment;
import com.medicalportal.model.AppointmentStatus;
import com.medicalportal.model.Doctor;
import com.medicalportal.model.Patient;
import com.medicalportal.repository.AppointmentRepository;
import com.medicalportal.repository.DoctorRepository;
import com.medicalportal.repository.PatientRepository;
import com.medicalportal.service.AppointmentService;
import com.medicalportal.util.IdGenerator;
import com.medicalportal.factory.AppointmentFactory;
import com.medicalportal.strategy.AppointmentValidationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for Patient Appointment Management.
 * Integrates:
 * - Strategy Pattern (AppointmentValidationContext) for decoupled, extensible business validations.
 * - Factory Pattern (AppointmentFactory) for centralized, consistent Appointment instantiation.
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentValidationContext validationContext;
    private final AppointmentFactory appointmentFactory;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository,
                                  AppointmentValidationContext validationContext,
                                  AppointmentFactory appointmentFactory) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.validationContext = validationContext;
        this.appointmentFactory = appointmentFactory;
    }

    @Override
    public AppointmentResponse bookAppointment(String patientId, AppointmentCreateRequest request) {
        // 1. Fetch patient
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient record not found."));

        // 2. Fetch doctor
        Doctor doctor = doctorRepository.findByDoctorId(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Selected doctor not found."));

        // 3. DESIGN PATTERN 1: STRATEGY PATTERN
        // Executes registered validation strategies (DateNotPast, DoctorSchedule, SlotConflict, PatientDoubleBooking)
        validationContext.executeValidation(patient, doctor, request.getAppointmentDate(), request.getAppointmentTime(), null);

        // 4. DESIGN PATTERN 2: FACTORY PATTERN
        // Centralizes instantiation, snapshot denormalization, status assignment, and audit timestamps
        Appointment appointment = appointmentFactory.createScheduledAppointment(
                patient, doctor, request.getAppointmentDate(), request.getAppointmentTime(), request.getReason()
        );

        Appointment saved = appointmentRepository.save(appointment);
        return new AppointmentResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(String patientId, AppointmentStatus status) {
        List<Appointment> list;
        if (status != null) {
            list = appointmentRepository.findByPatientIdAndStatusOrderByAppointmentDateDescAppointmentTimeDesc(patientId, status);
        } else {
            list = appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patientId);
        }
        return list.stream().map(AppointmentResponse::new).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse getAppointmentById(String patientId, String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentIdAndPatientId(appointmentId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found or you are not authorized to view it."));
        return new AppointmentResponse(appointment);
    }

    @Override
    public AppointmentResponse rescheduleAppointment(String patientId, String appointmentId, AppointmentRescheduleRequest request) {
        Appointment appointment = appointmentRepository.findByAppointmentIdAndPatientId(appointmentId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found or you are not authorized to modify it."));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot reschedule a cancelled appointment. Please book a new appointment instead.");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidOperationException("Cannot reschedule a completed appointment.");
        }

        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient record not found."));

        Doctor doctor = doctorRepository.findByDoctorId(appointment.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor record not found."));

        // DESIGN PATTERN 1: STRATEGY PATTERN
        // Reuses the encapsulated validation strategies with the target appointmentId for reschedule conflict checks
        validationContext.executeValidation(patient, doctor, request.getAppointmentDate(), request.getAppointmentTime(), appointmentId);

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime().trim());
        if (request.getReason() != null) {
            appointment.setReason(request.getReason().trim());
        }
        appointment.setUpdatedAt(Instant.now());

        Appointment updated = appointmentRepository.save(appointment);
        return new AppointmentResponse(updated);
    }

    @Override
    public AppointmentResponse cancelAppointment(String patientId, String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentIdAndPatientId(appointmentId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found or you are not authorized to modify it."));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidOperationException("This appointment is already cancelled.");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidOperationException("Cannot cancel an already completed appointment.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setUpdatedAt(Instant.now());

        Appointment updated = appointmentRepository.save(appointment);
        return new AppointmentResponse(updated);
    }

    @Override
    public void deleteAppointment(String patientId, String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentIdAndPatientId(appointmentId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found or you are not authorized to delete it."));

        appointmentRepository.delete(appointment);
    }
}
