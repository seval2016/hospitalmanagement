package com.project.service.business;

import com.project.entity.concretes.business.Department;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.DepartmentMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.DepartmentRequest;
import com.project.payload.response.business.DepartmentResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.DepartmentRepository;
import com.project.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<DepartmentResponse> saveDepartment(DepartmentRequest departmentRequest) {

        //!!! DepartmenName unique mi ?
        isDepartmentByDepartmentName(departmentRequest.getDepartmentName());

        //!!! DTO --> POJO
        Department savedDepartment = departmentRepository.save(departmentMapper.mapDepartmentRequestToDepartment(departmentRequest));

        return ResponseMessage.<DepartmentResponse>builder()
                .object(departmentMapper.mapDepartmentToDepartmentResponse(savedDepartment))
                .message(SuccessMessages.DEPARTMENT_SAVED)
                .httpStatus(HttpStatus.CREATED)
                .build();

    }

    private boolean isDepartmentByDepartmentName(String departmentName) {
        boolean departmentExist = departmentRepository.existsDepartmentByDepartmentNameEqualsIgnoreCase(departmentName);

        if (departmentExist) {
            throw new ConflictException(String.format(ErrorMessages.DEPARTMENT_ALREADY_EXIST_WITH_DEPARTMENT_NAME, departmentName));
        } else {
            return false;
        }
    }

    public ResponseMessage deleteDepartmentById(Long id) {

        isDepartmentExistById(id);
        departmentRepository.deleteById(id);

        return ResponseMessage.builder()
                .message(SuccessMessages.DEPARTMENT_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Department isDepartmentExistById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_DEPARTMENT_MESSAGE, id)));
    }

    public ResponseMessage<DepartmentResponse> getDepartmentByDepartmentName(String departmentName) {
        if (departmentRepository.getDepartmentByDepartmentName(departmentName).isPresent()) {

            return ResponseMessage.<DepartmentResponse>builder()
                    .message(SuccessMessages.DEPARTMENT_FOUND)
                    .object(departmentMapper.mapDepartmentToDepartmentResponse(
                            departmentRepository.getDepartmentByDepartmentName(departmentName).get()))
                    .build();
        } else {
            return ResponseMessage.<DepartmentResponse>builder()
                    .message(String.format(ErrorMessages.NOT_FOUND_DEPARTMENT_MESSAGE, departmentName))
                    .build();
        }
    }

    public Page<DepartmentResponse> findDepartmentByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return departmentRepository.findAll(pageable).map(departmentMapper::mapDepartmentToDepartmentResponse);
    }

    public Set<Department> getAllDepartmentByDepartmentId(Set<Long> idSet) {

        return idSet.stream()
                .map(this::isDepartmentExistById)
                .collect(Collectors.toSet());
    }

    public DepartmentResponse updateDepartmentById(Long departmentId, DepartmentRequest departmentRequest) {

        Department department=isDepartmentExistById(departmentId);

        // !!! requeste departman ismi degisti ise unique olmasi gerekiyor kontrolu

        if(
                !(department.getDepartmentName().equals(departmentRequest.getDepartmentName())) &&
                        (departmentRepository.existsByDepartmentName(departmentRequest.getDepartmentName()))
        ){
            throw new ConflictException(
                    String.format(ErrorMessages.DEPARTMENT_ALREADY_EXIST_WITH_DEPARTMENT_NAME,departmentRequest.getDepartmentName()));
        }
        //!!! DTO --> POJO
        Department updatedDepartment =departmentMapper.mapDepartmentRequestToUpdatedDepartment(departmentId,departmentRequest);

        //!!! DTO-POJO donusumunde setlenmeyen threatment plan verileri setleniyor, bunu yapmazsak DB deki bu deger
        // NULL olarak atanir

        // TODO: threatment plan setlenecek

        Department savedDepartment =departmentRepository.save(updatedDepartment);

        return departmentMapper.mapDepartmentToDepartmentResponse(savedDepartment);

    }
}
