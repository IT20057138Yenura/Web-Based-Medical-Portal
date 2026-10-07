package com.medicalportal.dto.response;

import com.medicalportal.model.Appointment;
import com.medicalportal.model.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDate;

public class AppointmentResponse {

    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String specialization;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String reason;
    private AppointmentStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public AppointmentResponse() {
    }

    public AppointmentResponse(Appointment appointment) {
        this.appointmentId = appointment.getAppointmentId();
        this.patientId = appointment.getPatientId();
        this.patientName = appointment.getPatientName();
        this.doctorId = appointment.getDoctorId();
        this.doctorName = appointment.getDoctorName();
        this.specialization = appointment.getSpecialization();
        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentTime = appointment.getAppointmentTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.createdAt = appointment.getCreatedAt();
        this.updatedAt = appointment.getUpdatedAt();
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
