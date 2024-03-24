package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.Day;
import lombok.*;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    // Tedavi planı bilgileri
    @NotNull(message = "Plan Details must not be empty")
    private String planDetails;

    @NotNull(message = "Start Date must not be empty")
    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "US")
    private LocalDate startDate;

    @NotNull(message = "End Date must not be empty")
    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "US")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    @NotNull(message = "Treatment Method must not be empty")
    private String treatmentMethod; //Tedavi yöntemi

    @NotNull(message = "Medications must not be empty")
    private List<String> medications;


    //- ilişki user tarafta yani treatmentPlanList field'ının bulunduğu yerde setleniyor.
     //- FetchType.EAGER ile treatmentPlan nesnesi oluşturulduğunda otomatik olarak getirilir (eager loading). Bu, ilgili treatmentPlan nesnesinin yüklenmesi sırasında ilişkili kullanıcıları (users) veritabanından çekilir ve bu kullanıcılar users alanına otomatik olarak atanır.

    @ManyToMany(mappedBy = "treatmentPlanList", fetch = FetchType.EAGER)
    private Set<User> users;

    //CascadeType.PERSIST özelliği ile işaretlenmişse, bir treatmentPlan oluşturulduğunda bu treatment plan ile ilişkilendirilmiş olan medical Record nesnesi de otomatik olarak persist edilecektir.
    // my note: içinde bulunduğum treatmentPlan kaydedilirken medical Record bilgisi de kaydedilsin istiyorsam CascadeType.PERSIST kısmını tetikliyoruz. Persist kalıcı hale getiriyor.

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MedicalRecord medicalRecord;

    //@ManyToMany ->Department
    @ManyToMany
    @JoinTable(
            name = "treatmentPlan_department",
            joinColumns = @JoinColumn(name = "treatmentPlan_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments;

}
