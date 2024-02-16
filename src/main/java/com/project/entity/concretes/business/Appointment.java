package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.concretes.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Randevu bilgileri
    private String appointmentDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate appointmentDateTime;

    @ManyToOne
    private User doctor;

    @ManyToOne
    private User patient;

    @ManyToOne
    private PatientInfo patientInfo;

    @OneToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany (mappedBy = "AppointmentList",fetch = FetchType.EAGER)
    private Set<User> users;

    @PreRemove
    private void removeAppointmentFromUser(){
        users.forEach(user ->user.getAppointmentList().remove(this)); //Bu kullanıcının appointmentlerini getir ve bunların arasında silmek istediğim nesneyi sil. Bu method bir appointment silmek istediğim zaman tetiklenecek ve gidip o appointment'i silecek.
    }
}

