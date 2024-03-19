package com.project.payload.mappers;

import com.project.entity.concretes.business.MedicalRecord;
import com.project.payload.request.business.MedicalRecordRequest;
import com.project.payload.response.business.MedicalRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    //DTO --
    public MedicalRecord mapMedicalRecordRequestToMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        return MedicalRecord.builder()
                .diagnosis(medicalRecordRequest.getDiagnosis())
                .prescription(medicalRecordRequest.getPrescription())
                .startDate(medicalRecordRequest.getStartDate())
                .endDate(medicalRecordRequest.getEndDate())
                .recordDate(medicalRecordRequest.getRecordDate())
                .labTestResults(medicalRecordRequest.getLabTestResults())
                .imagingTestResults(medicalRecordRequest.getImagingTestResults())
                .build();
    }

    public MedicalRecordResponse mapMedicalRecordToMedicalRecordResponse(MedicalRecord medicalRecord){
        return MedicalRecordResponse.builder()
                .id(medicalRecord.getId())
                .diagnosis(medicalRecord.getDiagnosis())
                .prescription(medicalRecord.getPrescription())
                .startDate(medicalRecord.getStartDate())
                .endDate(medicalRecord.getEndDate())
                .recordDate(medicalRecord.getRecordDate())
                .labTestResults(medicalRecord.getLabTestResults())
                .imagingTestResults(medicalRecord.getImagingTestResults())
                .build();
    }


}
