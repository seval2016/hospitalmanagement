package com.project.payload.mappers;

import com.project.entity.concretes.business.PatientInfo;
import com.project.payload.request.business.PatientInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientInfoMapper {

    @Autowired
    private UserMapper userMapper;

    public PatientInfo mapPatientInfoRequestToPatientInfo(PatientInfoRequest patientInfoRequest) {
    }
}
