package com.medicalportal.dto.response;

import com.medicalportal.model.Patient;

import java.time.Instant;
import java.time.LocalDate;

public class PatientResponse {

    private String patientId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private Instant createdAt;

    public PatientResponse() {
    }

    public PatientResponse(Patient patient) {
        this.patientId = patient.getPatientId();
        this.fullName = patient.getFullName();
        this.email = patient.getEmail();
        this.phone = patient.getPhone();
        this.dateOfBirth = patient.getDateOfBirth();
        this.gender = patient.getGender();
        this.address = patient.getAddress();
        this.createdAt = patient.getCreatedAt();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
