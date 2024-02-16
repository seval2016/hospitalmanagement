package com.project.entity.concretes.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.concretes.business.Appointment;
import com.project.entity.concretes.business.PatientInfo;
import com.project.entity.concretes.business.TreatmentPlan;
import com.project.entity.enums.Gender;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="t_user")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    @Pattern(regexp = "^(?!000|666|9\\d\\d)\\d{3}-(?!00)\\d{2}-(?!0000)\\d{4}$", message = "Invalid SSN format")
    private String ssn;

    private String name;
    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String address;

    @Column(unique = true)
    private String email;

    private Boolean built_in;

    private String fatherName;

    private String motherName;

    private int patientNumber;

    private boolean isActive;

    private Boolean isChiefDoctor; //doktor başhekim mi ?

    private Long doctorId; // bu hastalar için gerekli ,kendi doktorunun id si buraya yazılacak.

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.REMOVE) //mappedBy = "patient" de olabilir.
    // CascadeType.REMOVE ile bir user silindiğinde bu user'a ait bilgileri de silinsin.
    private List<PatientInfo> patientInfos;

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.REMOVE)
    private List<TreatmentPlan> treatmentPlans;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name="user_appointment",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "appointment_id")
    )
   private Set<Appointment> AppointmentList;




}
