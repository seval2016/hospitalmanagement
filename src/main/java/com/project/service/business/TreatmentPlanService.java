package com.project.service.business;

import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.business.TreatmentPlan;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.TreatmentPlanMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.TreatmentPlanRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.TreatmentPlanResponse;
import com.project.repository.business.TreatmentPlanRepository;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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


    public ResponseMessage<TreatmentPlanResponse> saveTreatmentPlan(TreatmentPlanRequest treatmentPlanRequest) {

        //!!! Treatment Plan'da olacak departman'ları Treatment Plan'dan getiriyorum
        Set<Department> departments = departmentService.getAllDepartmentByDepartmentId(treatmentPlanRequest.getDepartmentIdList());

        //!!! Medical Record bilgisi cekiliyor.
        MedicalRecord medicalRecord = medicalRecordService.findMedicalRecordById(treatmentPlanRequest.getMedicalRecordId());

        //!!! yukarda gelen department ici bos olma kontrolu :
        if (departments.isEmpty()) {

            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_DEPARTMENT_IN_LIST);
        }
        //!!! zaman kontrolu
        dateTimeValidator.checkDateWithException(treatmentPlanRequest.getStartDate(),
                treatmentPlanRequest.getEndDate());

        //!!! DTO -> POJO
        TreatmentPlan treatmentPlan=
                treatmentPlanMapper.mapTreatmentPlanRequestToTreatmentPlan(treatmentPlanRequest,departments,medicalRecord);

        TreatmentPlan savedTreatmentPlan=treatmentPlanRepository.save(treatmentPlan);
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


}
