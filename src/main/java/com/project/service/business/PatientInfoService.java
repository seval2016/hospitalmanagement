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
import com.project.payload.request.business.UpdatePatientInfoRequest;
import com.project.payload.response.business.PatientInfoResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.PatientInfoRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
        String doctorUsername = (String) httpServletRequest.getAttribute("username");

        //!!! requestte gelen patientId ile patient'i getirme
        User patient = methodHelper.isUserExist(patientInfoRequest.getPatientId());

        //!!! requestten gelen patientId gerçekten  bir patient'a mi ait
        methodHelper.checkRole(patient, RoleType.PATIENT);

        //!!! username ile patient getirme
        User doctor = userService.getDoctorByUsername(doctorUsername);

        // !!! requestten gelen departmentId ile department getiriyoruz
        Department department = departmentService.isDepartmentExistById(patientInfoRequest.getDepartmentId());

        // !!! requestten gelen medicalRecordId ile medicalRecord getiriyoruz
        MedicalRecord medicalRecord = medicalRecordService.findMedicalRecordById(patientInfoRequest.getMedicalRecordId());

        //!!! aynı department için duplicate controlü
        checkSameDepartment(patientInfoRequest.getPatientId(), department.getDepartmentName());

        //!!! DTO --> POJO
        PatientInfo patientInfo = patientInfoMapper.mapPatientInfoRequestToPatientInfo(patientInfoRequest);
        patientInfo.setPatient(patient);
        patientInfo.setMedicalRecord(medicalRecord);
        patientInfo.setDoctor(doctor);
        patientInfo.setDepartment(department);

        PatientInfo savedPatientInfo = patientInfoRepository.save(patientInfo);
        return ResponseMessage.<PatientInfoResponse>builder()
                .message(SuccessMessages.PATIENT_INFO_SAVED)
                .object(patientInfoMapper.mapPatientInfoToPatientInfoResponse(savedPatientInfo))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    private void checkSameDepartment(Long patientId, String departmentName) {
        boolean isDepartmentDuplicateExist =
                patientInfoRepository.getAllByPatientId_Id(patientId)
                        .stream()
                        .anyMatch(e -> e.getDepartment().getDepartmentName().equalsIgnoreCase(departmentName));

        if (isDepartmentDuplicateExist) {
            throw new ConflictException(String.format(ErrorMessages.DEPARTMENT_ALREADY_EXIST_WITH_DEPARTMENT_NAME, departmentName));
        }
    }

    public ResponseMessage deletePatientInfo(Long patientInfoId) {

        PatientInfo patientInfo = isPatientInfoExistById(patientInfoId);
        patientInfoRepository.deleteById(patientInfoId);

        return ResponseMessage.builder()
                .message(SuccessMessages.PATIENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public PatientInfo isPatientInfoExistById(Long id) {
        boolean isExist = patientInfoRepository.existsByIdEquals(id);

        if (!isExist) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.PATIENT_INFO_NOT_FOUND));
        } else {
            return patientInfoRepository.findById(id).get();
        }
    }

    public Page<PatientInfoResponse> getAllPatientInfoByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return patientInfoRepository.findAll(pageable)
                .map(patientInfoMapper::mapPatientInfoToPatientInfoResponse);
    }

    public List<PatientInfoResponse> getPatientInfoByPatientId(Long patientId) {
        User patient = methodHelper.isUserExist(patientId);

        //!!! Gelen user Patient rolüne sahip mi ?
        methodHelper.checkRole(patient, RoleType.PATIENT);

        //!!! Bu patient'e ait bir patientInfo var mı ?
        if (!patientInfoRepository.existsByPatient_IdEquals(patientId)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.PATIENT_INFO_NOT_FOUND_BY_PATIENT_ID, patientId)
            );
        }
        return patientInfoRepository.findByPatient_IdEquals(patientId)
                .stream()
                .map(patientInfoMapper::mapPatientInfoToPatientInfoResponse)
                .collect(Collectors.toList());
    }

    public PatientInfoResponse findPatientInfoById(Long patientInfoId) {
        return patientInfoMapper.mapPatientInfoToPatientInfoResponse(isPatientInfoExistById(patientInfoId));
    }

    public ResponseMessage<PatientInfoResponse> update(UpdatePatientInfoRequest patientInfoRequest, Long patientInfoId) {

        Department department = departmentService.isDepartmentExistById(patientInfoRequest.getDepartmentId());
        PatientInfo patientInfo = isPatientInfoExistById(patientInfoId);
        MedicalRecord medicalRecord = medicalRecordService.findMedicalRecordById(patientInfoRequest.getMedicalRecordId());

        //!!! DTO -> POJO
        PatientInfo patientInfoUpdate =
                patientInfoMapper.mapPatientInfoRequestUpdateToPatientInfo(patientInfoRequest, patientInfoId, department, medicalRecord);

        patientInfoUpdate.setDoctor(patientInfo.getDoctor());
        patientInfoUpdate.setPatient(patientInfo.getPatient());

        PatientInfo updatedPatientInfo = patientInfoRepository.save(patientInfoUpdate);

        return ResponseMessage.<PatientInfoResponse>builder()
                .message(SuccessMessages.PATIENT_INFO_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(patientInfoMapper.mapPatientInfoToPatientInfoResponse(updatedPatientInfo))
                .build();

    }

    public Page<PatientInfoResponse> getAllForDoctor(HttpServletRequest httpServletRequest, int page, int size) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        String userName = (String) httpServletRequest.getAttribute("username");

        return patientInfoRepository.findByDoctorId_UsernameEquals(userName, pageable)
                .map(patientInfoMapper::mapPatientInfoToPatientInfoResponse);

    }

    public Page<PatientInfoResponse> getAllForPatient(HttpServletRequest httpServletRequest, int page, int size) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        String userName = (String) httpServletRequest.getAttribute("username");

        return patientInfoRepository.findByPatientId_UsernameEquals(userName, pageable)
                .map(patientInfoMapper::mapPatientInfoToPatientInfoResponse);
    }
}
