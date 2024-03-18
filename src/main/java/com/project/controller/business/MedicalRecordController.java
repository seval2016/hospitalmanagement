package com.project.controller.business;

import com.project.payload.request.business.MedicalRecordRequest;
import com.project.payload.response.business.MedicalRecordResponse;
import com.project.service.business.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicalRecords")
public class MedicalRecordController {
    private MedicalRecordService medicalRecordService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')") //http://localhost:8080/medicalRecords/save
    public ResponseEntity<MedicalRecordResponse> saveMedicalRecord(@RequestBody @Valid MedicalRecordRequest MedicalRecordRequest) {
        return medicalRecordService.saveMedicalRecord(MedicalRecordRequest);
    }

}
