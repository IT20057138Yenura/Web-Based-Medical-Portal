# Web-Based Medical Portal
## Module: Patient Appointment Management
**Course**: SE2030 Software Engineering  
**Architecture**: Controller &rarr; Service &rarr; Repository &rarr; MongoDB  
**Technology Stack**: Java 21 LTS, Spring Boot 3.3.4, MongoDB Atlas / Local, HTML5, CSS3, JavaScript (Fetch API)

---

## 1. Project Overview & Scope

This module represents a self-contained, end-to-end implementation of the **Patient Appointment Management** major function for the Web-Based Medical Portal. It includes:
- **Public Doctor Directory & Availability**: Guests can search doctors, filter by specialization, and check real-time clinic slot availability before registration or login.
- **Patient Registration & Authentication**: Safe account creation with BCrypt password hashing, session-based login, logout, and protected patient-specific views.
- **Patient Profile CRUD**: View, update personal details, and delete patient accounts with confirmation dialogs.
- **Doctor Data Management**: Pre-seeded active medical specialists (Cardiology, Dermatology, Pediatrics, General Practice, Neurology, Endocrinology) with defined clinic days and hours.
- **Appointment Management CRUD**:
  - **Create**: Step-by-step booking wizard with 30-minute interval slot picker, double-booking prevention, and past-date validation.
  - **Read**: Tabbed appointment dashboard (`All`, `Scheduled`, `Completed`, `Cancelled`) and printable consultation slips.
  - **Update**: Reschedule date/time with instant conflict detection against other appointments.
  - **Cancel**: Releases booked time slot back to the public pool immediately.
  - **Delete**: Permanently clean up cancelled records.

---

## 2. Project Architecture & Structure

```
web-medical-portal/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/medicalportal/
    │   │   ├── MedicalPortalApplication.java
    │   │   ├── config/             (PasswordEncoderConfig, WebMvcConfig)
    │   │   ├── controller/         (AuthController, PatientController, DoctorController, AppointmentController)
    │   │   ├── service/            (AuthService, PatientService, DoctorService, AppointmentService + impls)
    │   │   ├── repository/         (PatientRepository, DoctorRepository, AppointmentRepository)
    │   │   ├── model/              (Patient, Doctor, Appointment, AppointmentStatus)
    │   │   ├── dto/                (Requests and Responses)
    │   │   ├── exception/          (GlobalExceptionHandler, Custom Exceptions)
    │   │   ├── security/           (AuthInterceptor, SessionContext)
    │   │   └── util/               (DataInitializer, IdGenerator)
    │   └── resources/
    │       ├── application.properties
    │       └── static/             (HTML5 pages, CSS stylesheet, JS clients)
```

---

## 3. Database Connection Configuration

The application reads the MongoDB connection string dynamically from configuration or the `MONGODB_URI` environment variable, ensuring credentials are never exposed in Git or frontend code.

---

## 4. Running the Application Locally

### Prerequisites
- **Java**: JDK 21 installed (`java -version`)
- **Maven**: Apache Maven 3.8+ installed (`mvn -version`)
- **Network**: Internet connection for MongoDB Atlas access (or local MongoDB on port 27017)

### Steps to Run
1. Open PowerShell and navigate to the project root:
   ```powershell
   cd "C:\Users\web-medical-portal"
   ```
2. Build and run the Spring Boot application:
   ```powershell
   mvn spring-boot:run
   ```
3. Open your web browser and navigate to:
   ```
   http://localhost:8080/
   ```


