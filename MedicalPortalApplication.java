package com.medicalportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.medicalportal.repository")
public class MedicalPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalPortalApplication.class, args);
    }
}
