package com.project.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.payload.response.user.DoctorResponse;
import com.project.payload.response.user.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.entity.enums.Day;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreatmentPlanResponse {

    private Long treatmentPlanId;
    private Day day;
    private String planDetails;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String treatmentMethod;
    private List<String> medications;
    private Set<Department> departments;
    private MedicalRecord medicalRecord;
    private Set<DoctorResponse> doctors;
    private Set <PatientResponse> patients;

}
