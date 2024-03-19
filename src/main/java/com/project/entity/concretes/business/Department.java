package com.project.entity.concretes.business;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Department Name must not be empty")
    private String departmentName;

    @ManyToMany
    @JsonIgnore
    private Set<TreatmentPlan> treatmentPlans;

    /* sonradan eklenecek doktorlar listesi
     @ManyToMany(mappedBy = "departments")
    private Set<Doctor> doctors;
     */

}
