package com.medicalportal.service.impl;

import com.medicalportal.dto.response.DoctorResponse;
import com.medicalportal.dto.response.TimeSlotResponse;
import com.medicalportal.exception.ResourceNotFoundException;
import com.medicalportal.model.Appointment;
import com.medicalportal.model.AppointmentStatus;
import com.medicalportal.model.Doctor;
import com.medicalportal.repository.AppointmentRepository;
import com.medicalportal.repository.DoctorRepository;
import com.medicalportal.service.DoctorService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<DoctorResponse> getAllDoctors(String specialization, String search) {
        List<Doctor> doctors;

        if (specialization != null && !specialization.trim().isEmpty() && !"All".equalsIgnoreCase(specialization.trim())) {
            doctors = doctorRepository.findBySpecializationIgnoreCaseAndStatus(specialization.trim(), "Active");
        } else if (search != null && !search.trim().isEmpty()) {
            doctors = doctorRepository.findByDoctorNameContainingIgnoreCaseAndStatus(search.trim(), "Active");
        } else {
            doctors = doctorRepository.findByStatus("Active");
        }

        if (search != null && !search.trim().isEmpty() && specialization != null && !specialization.trim().isEmpty() && !"All".equalsIgnoreCase(specialization.trim())) {
            String searchLower = search.trim().toLowerCase(Locale.ROOT);
            doctors = doctors.stream()
                    .filter(d -> d.getDoctorName().toLowerCase(Locale.ROOT).contains(searchLower))
                    .collect(Collectors.toList());
        }

        return doctors.stream()
                .map(DoctorResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse getDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return new DoctorResponse(doctor);
    }

    @Override
    public List<String> getSpecializations() {
        return doctorRepository.findByStatus("Active").stream()
                .map(Doctor::getSpecialization)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlotResponse> getAvailableSlots(String doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        if (!"Active".equalsIgnoreCase(doctor.getStatus())) {
            return Collections.emptyList();
        }

        // Check if doctor works on the requested day of week
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayName = dayOfWeek.name().substring(0, 1) + dayOfWeek.name().substring(1).toLowerCase(Locale.ROOT);

        boolean isWorkingDay = doctor.getAvailableDays().stream()
                .anyMatch(d -> d.equalsIgnoreCase(dayName));

        if (!isWorkingDay) {
            return Collections.emptyList();
        }

        // Parse working hours e.g. "09:00 - 13:00"
        String[] parts = doctor.getAvailableTime().split("-");
        if (parts.length != 2) {
            return Collections.emptyList();
        }

        LocalTime startTime = LocalTime.parse(parts[0].trim(), TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);

        // Fetch existing SCHEDULED appointments for this doctor on this date
        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatus(doctorId, date, AppointmentStatus.SCHEDULED);

        Set<String> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        // Generate 30-minute slots
        List<TimeSlotResponse> slots = new ArrayList<>();
        LocalTime current = startTime;
        while (current.plusMinutes(30).isBefore(endTime) || current.plusMinutes(30).equals(endTime)) {
            String timeString = current.format(TIME_FORMATTER);
            boolean isAvailable = !bookedTimes.contains(timeString);
            slots.add(new TimeSlotResponse(timeString, isAvailable));
            current = current.plusMinutes(30);
        }

        return slots;
    }
}
