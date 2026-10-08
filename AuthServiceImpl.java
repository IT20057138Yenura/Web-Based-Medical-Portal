package com.medicalportal.service.impl;

import com.medicalportal.dto.request.PatientLoginRequest;
import com.medicalportal.dto.request.PatientRegisterRequest;
import com.medicalportal.dto.response.PatientResponse;
import com.medicalportal.exception.DuplicateResourceException;
import com.medicalportal.exception.UnauthorizedException;
import com.medicalportal.model.Patient;
import com.medicalportal.repository.PatientRepository;
import com.medicalportal.security.SessionContext;
import com.medicalportal.service.AuthService;
import com.medicalportal.util.IdGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PatientResponse register(PatientRegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (patientRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("An account with email '" + normalizedEmail + "' already exists.");
        }

        // Generate unique patientId
        String patientId;
        do {
            patientId = IdGenerator.generatePatientId();
        } while (patientRepository.existsByPatientId(patientId));

        Patient patient = new Patient(
                patientId,
                request.getFullName().trim(),
                normalizedEmail,
                passwordEncoder.encode(request.getPassword()),
                request.getPhone().trim(),
                request.getDateOfBirth(),
                request.getGender().trim(),
                request.getAddress().trim()
        );

        Patient saved = patientRepository.save(patient);
        return new PatientResponse(saved);
    }

    @Override
    public PatientResponse login(PatientLoginRequest request, HttpSession session) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        Optional<Patient> optionalPatient = patientRepository.findByEmail(normalizedEmail);
        if (optionalPatient.isEmpty()) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        Patient patient = optionalPatient.get();
        if (!passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        // Store patient identification in HTTP session
        session.setAttribute(SessionContext.SESSION_PATIENT_ID, patient.getPatientId());
        session.setAttribute(SessionContext.SESSION_PATIENT_NAME, patient.getFullName());
        session.setAttribute(SessionContext.SESSION_PATIENT_EMAIL, patient.getEmail());

        return new PatientResponse(patient);
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public PatientResponse getCurrentPatient(HttpSession session) {
        if (session == null) {
            return null;
        }
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        if (patientId == null) {
            return null;
        }
        return patientRepository.findByPatientId(patientId)
                .map(PatientResponse::new)
                .orElse(null);
    }
}
