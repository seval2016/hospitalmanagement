package com.project.payload.mappers;

import com.project.entity.concretes.business.Department;
import com.project.payload.request.business.DepartmentRequest;
import com.project.payload.response.business.DepartmentResponse;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public Department mapDepartmentRequestToDepartment(DepartmentRequest departmentRequest) {
        return Department.builder()
                .departmentName(departmentRequest.getDepartmentName())
                .build();
    }

    public DepartmentResponse mapDepartmentToDepartmentResponse(Department department) {

        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .build();
    }

    public Department mapDepartmentRequestToUpdatedDepartment(Long departmentId, DepartmentRequest departmentRequest) {

        return Department.builder()
                .id(departmentId)
                .departmentName(departmentRequest.getDepartmentName())
                .build();
    }
}
