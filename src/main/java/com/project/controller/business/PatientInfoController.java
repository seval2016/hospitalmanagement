package com.project.controller.business;

import com.project.payload.request.business.PatientInfoRequest;
import com.project.payload.request.business.UpdatePatientInfoRequest;
import com.project.payload.response.business.PatientInfoResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.PatientInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/getAllPatientInfoByPage") // http://localhost:8080/patientInfo/getAllPatientInfoByPage?page=0&size=10&sort=id&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<PatientInfoResponse> getAllPatientInfoByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return patientInfoService.getAllPatientInfoByPage(page,size,sort,type);
    }

    @GetMapping("/getByPatientId/{patientId}") // http://localhost:8080/patientInfo/getByPatientId/3
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<PatientInfoResponse>> getPatientInfoByPatientId(@PathVariable Long patientId){
        List<PatientInfoResponse> patientInfoResponse=patientInfoService.getPatientInfoByPatientId(patientId);
        return ResponseEntity.ok(patientInfoResponse);
    }

    @GetMapping("/get/{patientInfoId}")  // http://localhost:8080/patientInfo/get/3
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<PatientInfoResponse> getPatientInfoById(@PathVariable Long patientInfoId){
        return ResponseEntity.ok(patientInfoService.findPatientInfoById(patientInfoId));
    }

    @GetMapping("/update/{patientInfoId}")// http://localhost:8080/patientInfo/update/2
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    public ResponseMessage<PatientInfoResponse> update (@RequestBody @Valid UpdatePatientInfoRequest patientInfoRequest,
                                                        @PathVariable Long patientInfoId){

        return patientInfoService.update(patientInfoRequest,patientInfoId);

    }

    @GetMapping("/getAllForDoctor") // http://localhost:8080/patientInfo/getAllForDoctor
    @PreAuthorize("hasAnyAuthority('DOCTOR')")
    public ResponseEntity<Page<PatientInfoResponse>> getAllForDoctor(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return new ResponseEntity<>(patientInfoService.getAllForDoctor(httpServletRequest,page,size), HttpStatus.OK);
    }

    @GetMapping("/getAllForPatient") // http://localhost:8080/patientInfo/getAllForPatient?page=0&size=10
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<Page<PatientInfoResponse>> getAllForPatient(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        return new ResponseEntity<>(patientInfoService.getAllForPatient(httpServletRequest, page, size), HttpStatus.OK);
    }

}
