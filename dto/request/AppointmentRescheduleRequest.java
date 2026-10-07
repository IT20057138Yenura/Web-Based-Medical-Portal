package com.medicalportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AppointmentRescheduleRequest {

    @NotNull(message = "Appointment date is required.")
    private LocalDate appointmentDate;

    @NotBlank(message = "Appointment time is required.")
    private String appointmentTime;

    @Size(max = 500, message = "Reason cannot exceed 500 characters.")
    private String reason;

    public AppointmentRescheduleRequest() {
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
}
