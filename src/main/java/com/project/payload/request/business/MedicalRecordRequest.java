package com.project.payload.request.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MedicalRecordRequest {

    //entity'den aldığımız veriler de id kaldırıyoruz çünkü kullanıcı id'yi bilemez ?
    //Bu bilgi db ye gitmeyecek service katında POJO ya çevrilecek ve tür dönüşümünden sonra db ye gidecek dolayısıyla format için anotation yazarsak gereksiz olur.

    @NotNull(message = "Diagnosis information must not be empty")
    private String diagnosis;

    @NotNull(message = "Prescription information must not be empty")
    private String prescription;

    @NotNull(message = "Start Date must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End Date must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Record Date must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate recordDate;

    @NotNull(message = "LabTestResults information must not be empty")
    private String labTestResults;//Laboratuvar test sonuçları

    @NotNull(message = "Imaging test results information must not be empty")
    private String imagingTestResults;//Görüntüleme test sonuçları
}
