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

    private String InfoNote;//Doktor hastası hakkında not tutmak isterse kullanacağı kısım

    private String insuranceCompanyName;

    private String insurancePolicyNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDate upcomingAppointments;//gelecek randevuları

     //PatientInfo bilgisini ilk etapta kullanacak roller hangisi ? doctor,patient O zaman doctor ve patient'ı bura ile ilişkilendirlecek. Ben patient Seval benım dahiliye PatientInfo'm olacak. KBB PatientInfo'm olacak vs vs. Benim tarafımdan bakarsam PatientInfo'yu many görüyorum. PatientInfo tarafına geçersem oradaki kbb yada java dahiliye infosu sadece bana ait.

    @ManyToOne //1 doctorun 1 den fazla patient info ya atanabilir
    @JsonIgnore
    private User doctor;

    @ManyToOne //1 hastanın 1 den fazla patient info ysu atanabilir
    @JsonIgnore
    private User patient;

    //Department'nın PatientInfo ile @ManyToOne ilişkisi var -> Bir departmanın birden fazla patient infosu olabilir.Şöyleki kbb düşünelim kbb ye gelen 100 tane hasta varsa aynı şekilde 100 tane de patientInfosu vardır.

    @ManyToOne
    private Department department;

    //Medical record ile @OnetoOne ilişki var tek taraflı yapılacak. Bu yüzden jsonignore gerek yok.

    @OneToOne
    private MedicalRecord medicalRecord;

}
