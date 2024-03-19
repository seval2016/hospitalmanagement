package com.project.controller.business;

import com.project.entity.concretes.business.Department;
import com.project.payload.request.business.DepartmentRequest;
import com.project.payload.response.business.DepartmentResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/save") //http://localhost:8080/department/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<DepartmentResponse> saveDepartment(@RequestBody @Valid DepartmentRequest departmentRequest){
       return departmentService.saveDepartment(departmentRequest);
    }

    @DeleteMapping("/delete/{id}")//http://localhost:8080/department/delete/2
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteDepartment(@PathVariable Long id){
        return departmentService.deleteDepartmentById(id);
    }

    @GetMapping("/getLessonByName")//http://localhost:8080/department/getLessonByName
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<DepartmentResponse> getDepartmentByDepartmentName(@RequestParam String departmentName){
        return departmentService.getDepartmentByDepartmentName(departmentName);
    }

    @GetMapping("/getDepartmentByName") // http://localhost:8080/department/findDepartmentByPage?page=0&size=10&sort=departmentName&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<DepartmentResponse> findDepartmentByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return departmentService.findDepartmentByPage(page,size,sort,type);
    }

    @GetMapping("/getAllDepartmentByDepartmentId") // http://localhost:8080/department/getAllDepartmentByDepartmentId?departmentId=1,2,3
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<Department> getAllDepartmentByDepartmentId(@RequestParam(name = "departmentId") Set<Long> idSet){
        return departmentService.getAllDepartmentByDepartmentId(idSet);
    }
    @PutMapping("/update/{departmentId}") //http://localhost:8080/department/update/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<DepartmentResponse> updateDepartmentById( @PathVariable Long departmentId,
                                                                    @RequestBody @Valid DepartmentRequest departmentRequest){
        return ResponseEntity.ok(departmentService.updateDepartmentById(departmentId,departmentRequest));
    }
}
