package com.project.controller.business;

import com.project.payload.request.business.TreatmentPlanRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.TreatmentPlanResponse;
import com.project.service.business.TreatmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/treatmentPlans")
public class TreatmentPlanController {
    private final TreatmentPlanService treatmentPlanService;

    @PostMapping("/save")  // http://localhost:8080/treatmentPlans/save  + POST  + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TreatmentPlanResponse> saveTreatmentPlan(@RequestBody @Valid TreatmentPlanRequest treatmentPlanRequest){
        return  treatmentPlanService.saveTreatmentPlan(treatmentPlanRequest);
    }

    @GetMapping("/getAll") // http://localhost:8080/treatmentPlans/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR','PATIENT')")
    public List<TreatmentPlanResponse> getAllTreatmentPlanList(){
        return treatmentPlanService.getAllTreatmentPlanByList();
    }

    @GetMapping("/getById/{id}")// http://localhost:8080/treatmentPlans/getById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public TreatmentPlanResponse getTreatmentPlanById(@PathVariable Long id){
        return treatmentPlanService.getTreatmentPlanById(id);

    }

    @GetMapping("/getAllUnassigned")// http://localhost:8080/treatmentPlans/getAllUnassigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR','PATIENT')")
    public List<TreatmentPlanResponse> getAllAssigned(){
        return treatmentPlanService.getAllAssigned();
    }

    @DeleteMapping("/delete/{id}")// http://localhost:8080/treatmentPlans/delete/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteTreatmentPlanById(@PathVariable Long id){
        return treatmentPlanService.deleteTreatmentPlanById(id);
    }

    @GetMapping("/getAllTreatmentPlanByPage")//http://localhost:8080/treatmentPlans/getAllTreatmentPlanByPage?page=0&size=1&sort=id&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR','PATIENT')")
    public Page<TreatmentPlanResponse> getAllTreatmentPlanByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type){
    return treatmentPlanService.getAllTreatmentPlanByPage(page,size,sort,type);
   }

    @GetMapping("/getAllTreatmentPlanByDoctor")
    @PreAuthorize("hasAnyAuthority('DOCTOR')")// http://localhost:8080/treatmentPlans/getAllTreatmentPlanByDoctor
    public Set<TreatmentPlanResponse> getAllTreatmentPlanByDoctorUsername(HttpServletRequest httpServletRequest){
        return treatmentPlanService.getAllTreatmentPlanByUser(httpServletRequest);
    }

    // bir Patient kendine ait treatment planları getiriyor
   @GetMapping("/getAllTreatmentPlanByPatient")// http://localhost:8080/treatmentPlans/getAllTreatmentPlanByPatient
   @PreAuthorize("hasAnyAuthority('PATIENT')")
   public Set<TreatmentPlanResponse> getAllTreatmentPlanByPatient(HttpServletRequest httpServletRequest){
        return treatmentPlanService.getAllTreatmentPlanByUser(httpServletRequest);
   }

   @GetMapping("/getAllTreatmentPlanDoctorId/{doctorId}")
   @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<TreatmentPlanResponse> getByDoctorId(@PathVariable Long doctorId){
        return treatmentPlanService.getByDoctorId(doctorId);
   }

   @GetMapping("/getAllTreatmentPlanDoctorId/{patientId}")
   @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<TreatmentPlanResponse> getByPatientId(@PathVariable Long patientId){
        return treatmentPlanService.getByPatientId(patientId);
   }










}
