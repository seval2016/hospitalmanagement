package com.project.controller.user;

import com.project.payload.request.business.ChooseTreatmentPlanWithId;
import com.project.payload.request.user.PatientRequest;
import com.project.payload.request.user.PatientRequestWithoutPassword;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.PatientResponse;
import com.project.service.user.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    @PostMapping("/save") //http://localhost:8080/patient/save
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<PatientResponse>> savePatient(@RequestBody @Valid PatientRequest patientRequest) {
        return ResponseEntity.ok(patientService.savePatient(patientRequest));
    }

    //!!! patient kendi bilgilerini guncellemesi
    @PatchMapping("/update") //http://localhost:8080/patient/update
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<String> updatePatient(@RequestBody @Valid PatientRequestWithoutPassword patientRequestWithoutPassword, HttpServletRequest request) {
        return patientService.updatePatient(patientRequestWithoutPassword, request);
    }

    @PutMapping("/update/{userId}") //http://localhost:8080/patient/update/2
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<PatientResponse> updatePatientForManagers(@PathVariable Long userId,
                                                                     @RequestBody @Valid PatientRequest patientRequest) {
        return patientService.updatePatientForManagers(patientRequest, userId);

    }

    //!!! Patient'lerin status degerini degistiren metod
    @GetMapping("/changeStatus")// http://localhost:8080/patient/changeStatus?id=3&status=true
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage changeStatusOfPatient(@RequestParam Long id, @RequestParam boolean status) {
        return patientService.changeStatusOfPatient(id, status);
    }

    // !!! Patient'e Treatment Plan eklemek istediğinde aşağıdaki endpoint tetiklenecek
    @PostMapping("/addTreatmentPlanToPatient")// http://localhost:8080/patient/addTreatmentPlanToPatient + JSON
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseMessage<PatientResponse> addTreatmentPlan(HttpServletRequest httpServletRequest,
                                                             @RequestBody @Valid ChooseTreatmentPlanWithId chooseTreatmentPlanWithId){

        return patientService.addTreatmentPlanToPatient(httpServletRequest,chooseTreatmentPlanWithId);
    }



}
