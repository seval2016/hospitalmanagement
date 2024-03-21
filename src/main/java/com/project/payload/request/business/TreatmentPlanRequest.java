package com.project.payload.request.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.entity.concretes.business.Department;
import com.project.entity.concretes.business.MedicalRecord;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TreatmentPlanRequest {

    @NotNull(message = "Please enter day")
    private Day day;

    @NotNull(message = "Plan Details must not be empty")
    private String planDetails;

    @NotNull(message = "Start Date must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "US")
    private LocalDateTime startDate;

    @NotNull(message = "End Date must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "US")
    private LocalDateTime endDate;

    @NotNull(message = "Please Select Department")
    @Size(min=1, message = "Department must not be empty")
    private Set<Long> departmentIdList;

    @NotNull(message = "Please enter medical record")
    private Long medicalRecordId;

}
