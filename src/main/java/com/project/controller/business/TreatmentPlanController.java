package com.project.controller.business;

import com.project.payload.request.business.TreatmentPlanRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.TreatmentPlanResponse;
import com.project.service.business.TreatmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','DOCTOR','PATIENT')")
    public List<TreatmentPlanResponse> getAllTreatmentPlanList(){
        return treatmentPlanService.getAllTreatmentPlanByList();
    }

}
