package com.project.payload.response.business;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MedicalRecordResponse {

    private Long id;

    private String diagnosis;

    private String prescription;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate recordDate;

    private String labTestResults;

    private String imagingTestResults;
}
