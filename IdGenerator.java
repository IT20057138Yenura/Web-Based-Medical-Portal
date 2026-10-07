package com.medicalportal.util;

import java.util.concurrent.ThreadLocalRandom;

public final class IdGenerator {

    private IdGenerator() {
    }

    public static String generatePatientId() {
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "PAT-" + rand;
    }

    public static String generateDoctorId() {
        int rand = ThreadLocalRandom.current().nextInt(100, 999);
        return "DOC-" + rand;
    }

    public static String generateAppointmentId() {
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "APT-" + rand;
    }
}
