package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.concretes.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosis;
    private String prescription;
    private LocalDateTime recordDate;

    @ManyToOne
    @JoinColumn(name = "patient_info_id")
    private PatientInfo patientInfo;

    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private Appointment appointment;

    @OneToOne
    @JoinColumn(name = "treatment_plan_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TreatmentPlan treatmentPlan;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

}
