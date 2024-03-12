package com.project.payload.request.user;

import com.project.entity.concretes.business.TreatmentPlan;
import com.project.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DoctorRequest extends BaseUserRequest {
    @NotNull(message = "Please select Threatment ")
    private Set<Long> treatmentIdList;

    @NotNull(message = "Please select Chief Doctor")
    private Boolean isChiefDoctor;

}
