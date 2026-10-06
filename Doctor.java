package com.medicalportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "doctors")
public class Doctor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String doctorId;

    private String doctorName;

    @Indexed
    private String specialization;

    private List<String> availableDays;

    private String availableTime;

    @Indexed
    private String status;

    private Instant createdAt;

    public Doctor() {
    }

    public Doctor(String doctorId, String doctorName, String specialization,
                  List<String> availableDays, String availableTime, String status) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
