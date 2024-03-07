package com.project.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.entity.concretes.business.TreatmentPlan;
import com.project.payload.response.abstracts.BaseUserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponse extends BaseUserResponse {

    private int patientNumber;
    private Set<TreatmentPlan> treatmentPlanSet;
    private String motherName;
    private String fatherName;
    private boolean isActive;
}
