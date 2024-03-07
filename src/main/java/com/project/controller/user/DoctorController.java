package com.project.controller.user;

import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.service.user.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private DoctorService doctorService;

    @PostMapping("/save") // http://localhost:8080/teacher/save
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@RequestBody @Valid DoctorRequest doctorRequest){
        return ResponseEntity.ok(doctorService.saveDoctor(doctorRequest));
    }

}
