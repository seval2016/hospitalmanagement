package com.project.controller.user;

import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.service.user.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private DoctorService doctorService;

    @PostMapping("/save") //http://localhost:8080/teacher/save
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@RequestBody @Valid DoctorRequest doctorRequest){
        return ResponseEntity.ok(doctorService.saveDoctor(doctorRequest));
    }


    @PutMapping("/update/{userId}")//http://localhost:8080/teacher/update/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<DoctorResponse> updateTeacherForManagers(@RequestBody @Valid DoctorRequest doctorRequest,@PathVariable Long userId){

        return doctorService.updateTeacherForManagers(doctorRequest,userId);


    }



}
