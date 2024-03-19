package com.project.repository.business;

import com.project.entity.concretes.business.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    boolean existsDepartmentByDepartmentNameEqualsIgnoreCase(String departmentName);

    Optional<Department> getDepartmentByDepartmentName(String departmentName);

    boolean existsByDepartmentName(String departmentName);
}
