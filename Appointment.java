package com.medicalportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "appointments")
@CompoundIndexes({
    @CompoundIndex(name = "doctor_slot_status_idx", def = "{'doctorId': 1, 'appointmentDate': 1, 'appointmentTime': 1, 'status': 1}")
})
public class Appointment {

    @Id
    private String id;

    @Indexed(unique = true)
    private String appointmentId;

    @Indexed
    private String patientId;

    private String patientName;

    @Indexed
    private String doctorId;

    private String doctorName;

    private String specialization;

    private LocalDate appointmentDate;

    private String appointmentTime;

    private String reason;

    @Indexed
    private AppointmentStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    public Appointment() {
    }

    public Appointment(String appointmentId, String patientId, String patientName,
                       String doctorId, String doctorName, String specialization,
                       LocalDate appointmentDate, String appointmentTime, String reason) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    // ==============================================================================
    // DESIGN PATTERN 3 (Bonus): BUILDER PATTERN (Creational Pattern - GoF)
    // ==============================================================================
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
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

        public Builder id(String id) { this.id = id; return this; }
        public Builder appointmentId(String appointmentId) { this.appointmentId = appointmentId; return this; }
        public Builder patientId(String patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder doctorId(String doctorId) { this.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { this.doctorName = doctorName; return this; }
        public Builder specialization(String specialization) { this.specialization = specialization; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder status(AppointmentStatus status) { this.status = status; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Appointment build() {
            Appointment a = new Appointment();
            a.setId(this.id);
            a.setAppointmentId(this.appointmentId);
            a.setPatientId(this.patientId);
            a.setPatientName(this.patientName);
            a.setDoctorId(this.doctorId);
            a.setDoctorName(this.doctorName);
            a.setSpecialization(this.specialization);
            a.setAppointmentDate(this.appointmentDate);
            a.setAppointmentTime(this.appointmentTime);
            a.setReason(this.reason);
            a.setStatus(this.status != null ? this.status : AppointmentStatus.SCHEDULED);
            a.setCreatedAt(this.createdAt != null ? this.createdAt : Instant.now());
            a.setUpdatedAt(this.updatedAt != null ? this.updatedAt : Instant.now());
            return a;
        }
    }
}

