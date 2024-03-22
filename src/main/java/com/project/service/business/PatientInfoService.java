package com.project.service.business;

import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.business.PatientInfo;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.PatientInfoMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.PatientInfoRequest;
import com.project.payload.response.business.PatientInfoResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.PatientInfoRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class PatientInfoService {

    private final PatientInfoRepository patientInfoRepository;
    private final MethodHelper methodHelper;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final MedicalRecordService medicalRecordService;
    private final PatientInfoMapper patientInfoMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<PatientInfoResponse> savePatientInfo(HttpServletRequest httpServletRequest, PatientInfoRequest patientInfoRequest) {

        //!!! requestten username getiriliyor.
        String doctorUsername= (String) httpServletRequest.getAttribute("username");

        //!!! requestte gelen patientId ile patient'i getirme
        User patient= methodHelper.isUserExist(patientInfoRequest.getPatientId());

        //!!! requestten gelen patientId gerçekten  bir patient'a mi ait
        methodHelper.checkRole(patient, RoleType.PATIENT);

        //!!! username ile patient getirme
        User doctor = userService.getDoctorByUserName(doctorUsername);

        // !!! requestten gelen departmentId ile department getiriyoruz
        Department department= departmentService.isDepartmentExistById(patientInfoRequest.getDepartmentId());

        // !!! requestten gelen medicalRecordId ile medicalRecord getiriyoruz
        MedicalRecord medicalRecord=medicalRecordService.findMedicalRecordById(patientInfoRequest.getMedicalRecordId());

        //!!! aynı department için duplicate controlü
        checkSameDepartment(patientInfoRequest.getPatientId(),department.getDepartmentName());

        //!!! DTO --> POJO
        PatientInfo patientInfo=patientInfoMapper.mapPatientInfoRequestToPatientInfo(patientInfoRequest);
        patientInfo.setPatient(patient);
        patientInfo.setMedicalRecord(medicalRecord);
        patientInfo.setDoctor(doctor);
        patientInfo.setDepartment(department);

        PatientInfo savedPatientInfo=patientInfoRepository.save(patientInfo);
        return ResponseMessage.<PatientInfoResponse>builder()
                .message(SuccessMessages.PATIENT_INFO_SAVED)
                .object(patientInfoMapper.mapPatientInfoTOPatientInfoResponse(savedPatientInfo))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    private void checkSameDepartment(Long patientId, String departmentName){
        boolean isDepartmentDuplicateExist =
                patientInfoRepository.getAllByPatientId_Id(patientId)
                        .stream()
                        .anyMatch(e->e.getDepartment().getDepartmentName().equalsIgnoreCase(departmentName));

        if(isDepartmentDuplicateExist){
            throw new ConflictException(String.format(ErrorMessages.DEPARTMENT_ALREADY_EXIST_WITH_DEPARTMENT_NAME, departmentName));
        }
    }

    public ResponseMessage deletePatientInfo(Long patientInfoId) {

        PatientInfo patientInfo= isPatientInfoExistById(patientInfoId);
        patientInfoRepository.deleteById(patientInfoId);

        return ResponseMessage.builder()
                .message(SuccessMessages.PATIENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public PatientInfo isPatientInfoExistById(Long id){
        boolean isExist=patientInfoRepository.existsByIdEquals(id);

        if(!isExist){
            throw new ResourceNotFoundException(String.format(ErrorMessages.PATIENT_INFO_NOT_FOUND));
        }else{
            return patientInfoRepository.findById(id).get();
        }
    }

}
