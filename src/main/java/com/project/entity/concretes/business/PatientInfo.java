package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.concretes.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PatientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String bloodType;

    @Column(unique = true)
    private String allergyInfo;

    private String currentCondition; //Hasta mevcut durumu ve sağlık sorunları.

    private String treatmentHistory; //Hasta tedavi geçmişi.

    private String currentMedications; //Hasta mevcut ilaçları.

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDate upcomingAppointments;

    @ManyToOne // 1 hastanın birden fazla patientinfosu olabilir
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private User patient; // patient adında bir User nesnesi

    @ManyToOne // 1 doktor birden fazla info ya atanabilir
    @JoinColumn(name = "doctor_id")
    private User doctor; // doctor adında bir User nesnesi

    @OneToMany(mappedBy = "patientInfo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patientInfo")
    private TreatmentPlan treatmentPlan;

}
