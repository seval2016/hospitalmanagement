package com.project.payload.mappers;

import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.business.PatientInfo;
import com.project.payload.request.business.PatientInfoRequest;
import com.project.payload.request.business.UpdatePatientInfoRequest;
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
                .infoNote(patientInfoRequest.getInfoNote())
                .insuranceCompanyName(patientInfoRequest.getInsuranceCompanyName())
                .insurancePolicyNumber(patientInfoRequest.getInsurancePolicyNumber())
                .upcomingAppointments(patientInfoRequest.getUpcomingAppointments())
                .build();
    }

    public PatientInfoResponse mapPatientInfoToPatientInfoResponse(PatientInfo patientInfo) {
        return PatientInfoResponse.builder()
                .id(patientInfo.getId())
                .departmentName(patientInfo.getDepartment().getDepartmentName())
                .bloodType(patientInfo.getBloodType())
                .allergyInfo(patientInfo.getAllergyInfo())
                .currentCondition(patientInfo.getCurrentCondition())
                .treatmentHistory(patientInfo.getTreatmentHistory())
                .currentMedications(patientInfo.getCurrentMedications())
                .infoNote(patientInfo.getInfoNote())
                .insuranceCompanyName(patientInfo.getInsuranceCompanyName())
                .insurancePolicyNumber(patientInfo.getInsurancePolicyNumber())
                .upcomingAppointments(patientInfo.getUpcomingAppointments())
                .patientResponse(userMapper.mapUserToPatientResponse(patientInfo.getPatient()))
                .build();
    }

    public PatientInfo mapPatientInfoRequestUpdateToPatientInfo(UpdatePatientInfoRequest patientInfoRequest,
                                                                Long patientInfoRequestId,
                                                                Department department,
                                                                MedicalRecord medicalRecord) {

        return PatientInfo.builder()
                .id(patientInfoRequestId)
                .infoNote(patientInfoRequest.getInfoNote())
                .medicalRecord(medicalRecord)
                .department(department)
                .bloodType(patientInfoRequest.getBloodType())
                .allergyInfo(patientInfoRequest.getAllergyInfo())
                .currentCondition(patientInfoRequest.getCurrentCondition())
                .treatmentHistory(patientInfoRequest.getTreatmentHistory())
                .currentMedications(patientInfoRequest.getCurrentMedications())
                .insuranceCompanyName(patientInfoRequest.getInsuranceCompanyName())
                .insurancePolicyNumber(patientInfoRequest.getInsurancePolicyNumber())
                .upcomingAppointments(patientInfoRequest.getUpcomingAppointments())
                .build();




    }
}
