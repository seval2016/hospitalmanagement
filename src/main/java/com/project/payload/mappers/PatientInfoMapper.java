package com.project.payload.mappers;

import com.project.entity.concretes.business.PatientInfo;
import com.project.payload.request.business.PatientInfoRequest;
import com.project.payload.response.business.PatientInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientInfoMapper {

    @Autowired
    private UserMapper userMapper;

    public PatientInfo mapPatientInfoRequestToPatientInfo(PatientInfoRequest patientInfoRequest) {

        return PatientInfo.builder()
                .bloodType(patientInfoRequest.getBloodType())
                .allergyInfo(patientInfoRequest.getAllergyInfo())
                .currentCondition(patientInfoRequest.getCurrentCondition())
                .treatmentHistory(patientInfoRequest.getTreatmentHistory())
                .currentMedications(patientInfoRequest.getCurrentMedications())
                .InfoNote(patientInfoRequest.getInfoNote())
                .insuranceCompanyName(patientInfoRequest.getInsuranceCompanyName())
                .insurancePolicyNumber(patientInfoRequest.getInsurancePolicyNumber())
                .upcomingAppointments(patientInfoRequest.getUpcomingAppointments())
                .build();
    }

    public PatientInfoResponse mapPatientInfoTOPatientInfoResponse(PatientInfo patientInfo) {
        return PatientInfoResponse.builder()
                .id(patientInfo.getId())
                .departmentName(patientInfo.getDepartment().getDepartmentName())
                .bloodType(patientInfo.getBloodType())
                .allergyInfo(patientInfo.getAllergyInfo())
                .currentCondition(patientInfo.getCurrentCondition())
                .treatmentHistory(patientInfo.getTreatmentHistory())
                .currentMedications(patientInfo.getCurrentMedications())
                .InfoNote(patientInfo.getInfoNote())
                .insuranceCompanyName(patientInfo.getInsuranceCompanyName())
                .insurancePolicyNumber(patientInfo.getInsurancePolicyNumber())
                .upcomingAppointments(patientInfo.getUpcomingAppointments())
                .patientResponse(userMapper.mapUserToPatientResponse(patientInfo.getPatient()))
                .build();
    }
}
