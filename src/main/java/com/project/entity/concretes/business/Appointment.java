package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.entity.concretes.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


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


    // Bir randevu bir doktora aittir
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctor_id")
    private User doctor;


    // Bir randevu bir hastaya aittir
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;




}

