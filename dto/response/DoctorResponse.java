package com.medicalportal.dto.response;

import com.medicalportal.model.Doctor;

import java.util.List;

public class DoctorResponse {

    private String doctorId;
    private String doctorName;
    private String specialization;
    private List<String> availableDays;
    private String availableTime;
    private String status;

    public DoctorResponse() {
    }

    public DoctorResponse(Doctor doctor) {
        this.doctorId = doctor.getDoctorId();
        this.doctorName = doctor.getDoctorName();
        this.specialization = doctor.getSpecialization();
        this.availableDays = doctor.getAvailableDays();
        this.availableTime = doctor.getAvailableTime();
        this.status = doctor.getStatus();
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
}
