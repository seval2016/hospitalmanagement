package com.project.controller.business;

import com.project.payload.request.business.MedicalRecordRequest;
import com.project.payload.response.business.MedicalRecordResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicalRecords")
public class MedicalRecordController {
    private MedicalRecordService medicalRecordService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')") //http://localhost:8080/medicalRecords/save
    public ResponseMessage<MedicalRecordResponse> saveMedicalRecord(@RequestBody @Valid MedicalRecordRequest MedicalRecordRequest) {
        return medicalRecordService.saveMedicalRecord(MedicalRecordRequest);
    }

    @GetMapping("/{id}")//http://localhost:8080/medicalRecords/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR')")
    public MedicalRecordResponse getMedicalRecordById(@PathVariable Long id){
        return medicalRecordService.getMedicalRecordById(id);
    }

    @GetMapping("/getAll")//http://localhost:8080/medicalRecords/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR')")
    public List<MedicalRecordResponse> getAllMedicalRecords(){
        return medicalRecordService.getAllMedicalRecords();
    }

}
