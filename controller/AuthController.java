package com.medicalportal.controller;

import com.medicalportal.dto.request.PatientLoginRequest;
import com.medicalportal.dto.request.PatientRegisterRequest;
import com.medicalportal.dto.response.ApiResponse;
import com.medicalportal.dto.response.PatientResponse;
import com.medicalportal.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PatientResponse>> register(@Valid @RequestBody PatientRegisterRequest request) {
        PatientResponse registered = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful! You can now log in.", registered));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<PatientResponse>> login(@Valid @RequestBody PatientLoginRequest request,
                                                              HttpSession session) {
        PatientResponse patient = authService.login(request, session);
        return ResponseEntity.ok(ApiResponse.success("Login successful. Welcome back, " + patient.getFullName() + "!", patient));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.success("You have been successfully logged out."));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<PatientResponse>> checkSession(HttpSession session) {
        PatientResponse patient = authService.getCurrentPatient(session);
        if (patient == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Not authenticated", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Authenticated", patient));
    }
}
