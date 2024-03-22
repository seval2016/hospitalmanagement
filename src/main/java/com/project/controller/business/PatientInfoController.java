package com.project.controller.business;

import com.project.payload.request.business.PatientInfoRequest;
import com.project.payload.response.business.PatientInfoResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.PatientInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patientInfo")
public class PatientInfoController {

    private final PatientInfoService patientInfoService;

    @PostMapping("/save") // http://localhost:8080/patientInfo/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('DOCTOR')")
    public ResponseMessage<PatientInfoResponse> savePatientInfo(HttpServletRequest httpServletRequest,
                                                                @RequestBody @Valid PatientInfoRequest patientInfoRequest){
        return patientInfoService.savePatientInfo(httpServletRequest,patientInfoRequest);
    }
    @DeleteMapping("/delete/{patientInfoId}")// http://localhost:8080/patientInfo/delete/3 + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    public ResponseMessage delete(@PathVariable Long  patientInfoId){
        return patientInfoService.deletePatientInfo(patientInfoId);
    }

}
