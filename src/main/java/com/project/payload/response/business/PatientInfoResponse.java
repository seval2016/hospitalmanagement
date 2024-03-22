package com.project.payload.response.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.user.User;
import com.project.payload.response.user.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientInfoResponse {

    private Long id;
    private String bloodType;
    private String allergyInfo;
    private String currentCondition;
    private String treatmentHistory;
    private String currentMedications;
    private String InfoNote;
    private String insuranceCompanyName;
    private String insurancePolicyNumber;
    private LocalDate upcomingAppointments;
    private String departmentName;
    private Long medicalRecordId;
    private PatientResponse patientResponse;
}
