package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.concretes.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
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

    private String departmentName;

    @ManyToMany
    @JsonIgnore
    private Set<TreatmentPlan> treatmentPlans;

}
