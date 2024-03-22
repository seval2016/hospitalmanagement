package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PatientInfoRequest {

    @NotNull(message = "Please select medical record")
    private Long medicalRecordId;

    @NotNull(message = "Blood Type must not be empty")
    private String bloodType;

    @NotNull(message = "Allergy Info must not be empty")
    private String allergyInfo;

    @NotNull(message = "Current Condition must not be empty")
    private String currentCondition;

    @NotNull(message = "Treatment History must not be empty")
    private String treatmentHistory;

    @NotNull(message = "Current Medications must not be empty")
    private String currentMedications;

    @NotNull(message = "Please enter info")
    @Size(min = 10, max = 200, message = "Info should be at least 10 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+" ,message="Info must consist of the characters .")
    private String InfoNote;

    @NotNull(message = "Insurance company name must not be empty")
    private String insuranceCompanyName;

    @NotNull(message = "Insurance Policy name must not be empty")
    private String insurancePolicyNumber;

    private LocalDate upcomingAppointments;

    @NotNull(message = "Please select patient")
    private Long patientId;

    @NotNull(message = "Please select department")
    private Long departmentId;


}
