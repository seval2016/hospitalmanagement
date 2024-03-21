package com.project.payload.mappers;

import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.business.TreatmentPlan;
import com.project.payload.request.business.TreatmentPlanRequest;
import com.project.payload.response.business.TreatmentPlanResponse;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TreatmentPlanMapper {
    public TreatmentPlan mapTreatmentPlanRequestToTreatmentPlan(TreatmentPlanRequest treatmentPlanRequest, Set<Department> departments, MedicalRecord medicalRecord) {

        return TreatmentPlan.builder()
                .day(treatmentPlanRequest.getDay())
                .planDetails(treatmentPlanRequest.getPlanDetails())
                .startDate(treatmentPlanRequest.getStartDate())
                .endDate(treatmentPlanRequest.getEndDate())
                .departments(departments)
                .medicalRecord(medicalRecord)
                .build();

    }

    public TreatmentPlanResponse mapTreatmentPlanToTreatmentPlanResponse(TreatmentPlan treatmentPlan) {
        return TreatmentPlanResponse.builder()
                .treatmentPlanId(treatmentPlan.getId())
                .day(treatmentPlan.getDay())
                .planDetails(treatmentPlan.getPlanDetails())
                .startDate(treatmentPlan.getStartDate())
                .endDate(treatmentPlan.getEndDate())
                .treatmentMethod(treatmentPlan.getTreatmentMethod())
                .medications(treatmentPlan.getMedications())
                .departments(treatmentPlan.getDepartments())
                .build();
    }
}
