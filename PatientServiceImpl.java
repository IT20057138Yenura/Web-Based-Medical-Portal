package com.medicalportal.service.impl;

import com.medicalportal.dto.request.PatientUpdateRequest;
import com.medicalportal.dto.response.PatientResponse;
import com.medicalportal.exception.ResourceNotFoundException;
import com.medicalportal.model.Patient;
import com.medicalportal.repository.AppointmentRepository;
import com.medicalportal.repository.PatientRepository;
import com.medicalportal.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public PatientResponse getProfile(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));
        return new PatientResponse(patient);
    }

    @Override
    public PatientResponse updateProfile(String patientId, PatientUpdateRequest request) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));

        patient.setFullName(request.getFullName().trim());
        patient.setPhone(request.getPhone().trim());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender().trim());
        patient.setAddress(request.getAddress().trim());
        patient.setUpdatedAt(Instant.now());

        Patient updated = patientRepository.save(patient);
        return new PatientResponse(updated);
    }

    @Override
    public void deleteAccount(String patientId, HttpSession session) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));

        // Remove patient appointments
        appointmentRepository.deleteByPatientId(patientId);

        // Remove patient record
        patientRepository.delete(patient);

        // Invalidate current session
        if (session != null) {
            session.invalidate();
        }
    }
}
