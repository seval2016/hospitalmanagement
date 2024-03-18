package com.project.service.business;

import com.project.entity.concretes.business.MedicalRecord;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.MedicalRecordRequest;
import com.project.payload.response.business.MedicalRecordResponse;
import com.project.repository.business.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public ResponseEntity<MedicalRecordResponse> saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {

        //!!! kayıtlarda zaman çakışmalarını önlemek için validation yapıldı
        validateMedicalRecordDates(medicalRecordRequest);


    }

    private void validateMedicalRecordDatesForRequest(MedicalRecordRequest medicalRecordRequest) {
        // !!! bu metodda amacimiz, request'ten gelen recordDate,StartDate ve endDate arasindaki tarih sirasina gore dogru mu setlenmis onu kontrol etmek

        //recordDate >start -> hasta kaydımız start date den daha sonra olamaz

        if (medicalRecordRequest.getRecordDate().isAfter(medicalRecordRequest.getStartDate())) {
            throw new BadRequestException(ErrorMessages.MEDICAL_RECORD_START_DATE_IS_EARLIER_THAN_LAST_RECORD_DATE);
        }

        // end > start -> Medical record'un  bitiş tarihi başlangıçtan büyük olamaz
        if (medicalRecordRequest.getEndDate().isBefore(medicalRecordRequest.getStartDate())) {
            throw new BadRequestException((ErrorMessages.MEDICAL_RECORD_END_DATE_IS_EARLIER_THAN_START_DATE));
        }
    }

    private void validateMedicalRecordDates(MedicalRecordRequest medicalRecordRequest) {
        validateMedicalRecordDatesForRequest(medicalRecordRequest);

        //!!! yıl içerisine eklenecek Medical Record ,mevcuttakilerin tarihleri ile çakışmamalı
        // Bunun için ilk olarak medicalRecordRequest'a gelen yıl bilgisini almalıyız. Örneğin 2024 yılına ait bir request geldi bunun için önce db ye gidip tüm 2024 kayıtlarını getirmeliyiz ki çakışma var mı onun kontrolünü yapmalıyız.

        if (medicalRecordRepository.findByYear(medicalRecordRequest.getStartDate().getYear())
                .stream()
                .anyMatch(medicalRecord ->
                        (        medicalRecord.getStartDate().equals(medicalRecordRequest.getStartDate()) //!!! 1. kontrol : baslama tarihleri ayni ise --> mr1(10 kasim 2023) / Yeni eklenen MR(10 kasim 2023)
                            || (medicalRecord.getStartDate().isBefore(medicalRecordRequest.getStartDate()) //!!! 2. kontrol : Request'den gelen baslama tarihi db'de bulunan mr'nin baslama ve bitis tarihi ortasinda ise -->
                                      && medicalRecord.getEndDate().isAfter(medicalRecordRequest.getStartDate())) // Ornek : mr1 ( baslama 10 kasim 2023 - bitme 20 kasim 2023)  - Yeni MR ( baslama 15 kasim 2023 bitme 25 kasim 2023)
                            || (medicalRecord.getStartDate().isBefore(medicalRecordRequest.getEndDate()) //!!! 3. kontrol bitis tarihi  mevcuttun baslama ve bitis tarihi ortasinda ise
                                       && medicalRecord.getEndDate().isAfter(medicalRecordRequest.getEndDate()))// Ornek : mr1 ( baslama 10 kasim 2023 - bitme 20 kasim 2023)  - Yeni MR ( baslama 09 kasim 2023 bitme 15 kasim 2023)
                            || (medicalRecord.getStartDate().isAfter(medicalRecordRequest.getStartDate())  //!!!4.kontrol : yeni eklenecek eskiyi tamamen kapsiyorsa
                                       && medicalRecord.getEndDate().isBefore(medicalRecordRequest.getEndDate()))//mr1 ( baslama 10 kasim 2023 - bitme 20 kasim 2023)  - Yeni MR ( baslama 09 kasim 2023 bitme 25 kasim 2023)

                ))
        ){
            throw new BadRequestException(ErrorMessages.MEDICAL_RECORD_CONFLICT_MESSAGE);

        }


    }


}
