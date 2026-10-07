package com.medicalportal.util;

import com.medicalportal.model.Doctor;
import com.medicalportal.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final DoctorRepository doctorRepository;

    public DataInitializer(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        if (doctorRepository.count() == 0) {
            log.info("Doctor collection is empty. Seeding initial doctor dataset...");

            List<Doctor> initialDoctors = Arrays.asList(
                new Doctor(
                    "DOC-101",
                    "Dr. Eleanor Vance",
                    "Cardiology",
                    Arrays.asList("Monday", "Wednesday", "Friday"),
                    "09:00 - 13:00",
                    "Active"
                ),
                new Doctor(
                    "DOC-102",
                    "Dr. Marcus Brody",
                    "Dermatology",
                    Arrays.asList("Tuesday", "Thursday", "Saturday"),
                    "10:00 - 14:00",
                    "Active"
                ),
                new Doctor(
                    "DOC-103",
                    "Dr. Sarah Chen",
                    "Pediatrics",
                    Arrays.asList("Monday", "Tuesday", "Thursday"),
                    "08:30 - 12:30",
                    "Active"
                ),
                new Doctor(
                    "DOC-104",
                    "Dr. James Wilson",
                    "General Practice",
                    Arrays.asList("Monday", "Wednesday", "Thursday", "Friday"),
                    "09:00 - 14:00",
                    "Active"
                ),
                new Doctor(
                    "DOC-105",
                    "Dr. Gregory House",
                    "Neurology",
                    Arrays.asList("Wednesday", "Friday", "Saturday"),
                    "13:00 - 17:00",
                    "Active"
                ),
                new Doctor(
                    "DOC-106",
                    "Dr. Lisa Cuddy",
                    "Endocrinology",
                    Arrays.asList("Tuesday", "Wednesday", "Friday"),
                    "09:30 - 13:30",
                    "Active"
                )
            );

            doctorRepository.saveAll(initialDoctors);
            log.info("Successfully seeded {} doctors.", initialDoctors.size());
        } else {
            log.info("Doctors already exist in database (count: {}). Skipping seed.", doctorRepository.count());
        }
    }
}
