package com.medicalportal.controller;

import com.medicalportal.dto.request.PatientUpdateRequest;
import com.medicalportal.dto.response.ApiResponse;
import com.medicalportal.dto.response.PatientResponse;
import com.medicalportal.security.SessionContext;
import com.medicalportal.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PatientResponse>> getProfile(HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        PatientResponse profile = patientService.getProfile(patientId);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully.", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<PatientResponse>> updateProfile(@Valid @RequestBody PatientUpdateRequest request,
                                                                      HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        PatientResponse updated = patientService.updateProfile(patientId, request);
        // Update session name if changed
        session.setAttribute(SessionContext.SESSION_PATIENT_NAME, updated.getFullName());
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully.", updated));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        patientService.deleteAccount(patientId, session);
        return ResponseEntity.ok(ApiResponse.success("Your account and associated appointments have been successfully deleted."));
    }
}
