package com.medicalportal.controller;

import com.medicalportal.dto.response.ApiResponse;
import com.medicalportal.dto.response.DoctorResponse;
import com.medicalportal.dto.response.TimeSlotResponse;
import com.medicalportal.service.DoctorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String search) {
        List<DoctorResponse> doctors = doctorService.getAllDoctors(specialization, search);
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully.", doctors));
    }

    @GetMapping("/specializations")
    public ResponseEntity<ApiResponse<List<String>>> getSpecializations() {
        List<String> specializations = doctorService.getSpecializations();
        return ResponseEntity.ok(ApiResponse.success("Specializations retrieved successfully.", specializations));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable String doctorId) {
        DoctorResponse doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor details retrieved.", doctor));
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> getAvailableSlots(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlotResponse> slots = doctorService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(ApiResponse.success("Slots calculated successfully.", slots));
    }
}
