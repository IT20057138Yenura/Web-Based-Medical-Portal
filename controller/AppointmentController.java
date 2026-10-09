package com.medicalportal.controller;

import com.medicalportal.dto.request.AppointmentCreateRequest;
import com.medicalportal.dto.request.AppointmentRescheduleRequest;
import com.medicalportal.dto.response.ApiResponse;
import com.medicalportal.dto.response.AppointmentResponse;
import com.medicalportal.model.AppointmentStatus;
import com.medicalportal.security.SessionContext;
import com.medicalportal.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @Valid @RequestBody AppointmentCreateRequest request,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        AppointmentResponse appointment = appointmentService.bookAppointment(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment successfully scheduled!", appointment));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        List<AppointmentResponse> list = appointmentService.getPatientAppointments(patientId, status);
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @PathVariable String id,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        AppointmentResponse appointment = appointmentService.getAppointmentById(patientId, id);
        return ResponseEntity.ok(ApiResponse.success("Appointment details retrieved.", appointment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> rescheduleAppointment(
            @PathVariable String id,
            @Valid @RequestBody AppointmentRescheduleRequest request,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        AppointmentResponse updated = appointmentService.rescheduleAppointment(patientId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Appointment successfully rescheduled!", updated));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @PathVariable String id,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        AppointmentResponse cancelled = appointmentService.cancelAppointment(patientId, id);
        return ResponseEntity.ok(ApiResponse.success("Appointment has been cancelled.", cancelled));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(
            @PathVariable String id,
            HttpSession session) {
        String patientId = (String) session.getAttribute(SessionContext.SESSION_PATIENT_ID);
        appointmentService.deleteAppointment(patientId, id);
        return ResponseEntity.ok(ApiResponse.success("Appointment record deleted successfully."));
    }
}
