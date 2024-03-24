package com.project.service.business;

import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.business.TreatmentPlan;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.TreatmentPlanMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.TreatmentPlanRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.TreatmentPlanResponse;
import com.project.repository.business.TreatmentPlanRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentPlanService {
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final DepartmentService departmentService;
    private final MedicalRecordService medicalRecordService;
    private final DateTimeValidator dateTimeValidator;
    private final TreatmentPlanMapper treatmentPlanMapper;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;

    public ResponseMessage<TreatmentPlanResponse> saveTreatmentPlan(TreatmentPlanRequest treatmentPlanRequest) {

        //!!! Treatment Plan'da olacak departman'ları Treatment Plan'dan getiriyorum
        Set<Department> departments = departmentService.getAllDepartmentByDepartmentId(treatmentPlanRequest.getDepartmentIdList());

        //!!! Medical Record bilgisi cekiliyor.
        MedicalRecord medicalRecord = medicalRecordService.findMedicalRecordById(treatmentPlanRequest.getMedicalRecordId());

        //!!! yukarıda gelen department ici bos olma kontrolu :
        if (departments.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_DEPARTMENT_IN_LIST);
        }
        //!!! zaman kontrolu
        dateTimeValidator.checkDateWithException(treatmentPlanRequest.getStartTime(),treatmentPlanRequest.getStopTime());

        //!!! DTO -> POJO
        TreatmentPlan treatmentPlan =
                treatmentPlanMapper.mapTreatmentPlanRequestToTreatmentPlan(treatmentPlanRequest, departments, medicalRecord);

        TreatmentPlan savedTreatmentPlan = treatmentPlanRepository.save(treatmentPlan);
        return ResponseMessage.<TreatmentPlanResponse>builder()
                .message(SuccessMessages.TREATMENT_PLAN_SAVED)
                .httpStatus(HttpStatus.CREATED)
                .object(treatmentPlanMapper.mapTreatmentPlanToTreatmentPlanResponse(savedTreatmentPlan))
                .build();
    }

    public List<TreatmentPlanResponse> getAllTreatmentPlanByList() {

        return treatmentPlanRepository.findAll()
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toList());
    }

    public TreatmentPlanResponse getTreatmentPlanById(Long id) {
        return treatmentPlanMapper.mapTreatmentPlanToTreatmentPlanResponse(isTreatmentPlanExistById(id));
    }

    private TreatmentPlan isTreatmentPlanExistById(Long id) {
        return treatmentPlanRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_TREATMENT_PLAN_MESSAGE, id)));
    }

    public List<TreatmentPlanResponse> getAllUnassigned() {
        return treatmentPlanRepository.findByUsers_IdNull()
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toList());
    }

    public List<TreatmentPlanResponse> getAllAssigned() {
        return treatmentPlanRepository.findByUsers_IdNotNull()
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteTreatmentPlanById(Long id) {
        isTreatmentPlanExistById(id);
        treatmentPlanRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.TREATMENT_PLAN_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<TreatmentPlanResponse> getAllTreatmentPlanByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return treatmentPlanRepository.findAll(pageable)
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse);

    }

    public Set<TreatmentPlanResponse> getAllTreatmentPlanByUser(HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");

        //User tablosunda Users sütunun Username'i httpServletRequest'den gelen username'e eşit ise
        return treatmentPlanRepository.getTreatmentPlanByUsersUsername(userName)
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toSet());
    }

    public Set<TreatmentPlanResponse> getByDoctorId(Long doctorId) {
        User doctor = methodHelper.isUserExist(doctorId);
        methodHelper.checkRole(doctor, RoleType.DOCTOR);
        return treatmentPlanRepository.findByUsers_IdEquals(doctorId)
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toSet());
    }

    public Set<TreatmentPlanResponse> getByPatientId(Long patientId) {
        User patient = methodHelper.isUserExist(patientId);
        methodHelper.checkRole(patient, RoleType.PATIENT);

        return treatmentPlanRepository.findByUsers_IdEquals(patientId)
                .stream()
                .map(treatmentPlanMapper::mapTreatmentPlanToTreatmentPlanResponse)
                .collect(Collectors.toSet());
    }

    //!!! Doctor service için yazıldı
    public Set<TreatmentPlan> getTreatmentPlanById(Set<Long> treatmentPlanIdSet) {
        Set<TreatmentPlan> treatmentPlans = treatmentPlanRepository.getTreatmentPlanByTreatmenPlanIdList(treatmentPlanIdSet);

        if (treatmentPlans.isEmpty()) {
            throw new BadRequestException(ErrorMessages.NOT_FOUND_TREATMENT_PLAN_MESSAGE_WITHOUT_ID_INFO);
        }
        return treatmentPlans;
    }


}