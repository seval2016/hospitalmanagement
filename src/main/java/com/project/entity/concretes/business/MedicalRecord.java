package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Diagnosis information must not be empty")
    private String diagnosis; //teşhis

    @NotNull(message = "Prescription information must not be empty")
    private String prescription;// reçete

    @NotNull(message = "Record Date  must not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate recordDate; //kayıt tarihi

    private String labTestResults;//Laboratuvar test sonuçları

    private String imagingTestResults;//Görüntüleme test sonuçları


     //burada treatmentplan ilişkisi kurulacak.
     //Bir medicalRecord kaydediliyorsa treatmenPlan'larda kaydedilsin eğer siliniyorsa treatmenPlan'lar da silinsin demek istiyorsak CascadeType.ALL kullanırız.

     //JsonProperty kısmında jsonignore da diyebiliriz. Bu kısımda response tarafına yazılmasını engeller ve bu durum sonsun döngü hatasını almamamızı sağlar.

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<TreatmentPlan> treatmenPlans;


}
