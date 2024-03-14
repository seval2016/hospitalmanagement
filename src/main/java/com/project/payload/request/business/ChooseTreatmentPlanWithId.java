package com.project.payload.request.business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChooseTreatmentPlanWithId {
    @NotNull(message = "Please select Treatment Plan")
    @Size(min = 1, message = "Lesson must not be empty")
    private Set<Long> treatmentPlanId;
}