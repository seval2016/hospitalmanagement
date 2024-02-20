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

    /*
   Bu classın sadece userlar ile alakası var. Bir doctorun birden fazla randevusu olabilir
    */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User doctor;

     /*
    Bir öğrenci birden fazla rehberlik toplantısına katılabildiği gibi bir toplantıya birden fazla öğrenci katılabilir.
     Bu ksımda özellikle customize edilir çünkü eger student_id diye belirtmezsek user_id olarak alır bu da güzel olmaz */
    @ManyToMany
    @JoinTable(
            name = "appointment_patient_table",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private List<User> patientList;




}

