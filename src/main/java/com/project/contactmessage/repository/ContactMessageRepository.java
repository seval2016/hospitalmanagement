package com.project.contactmessage.repository;

import com.project.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository //Burda repository yerine component yazsak yine çalışırdı fakat repository katmanına has bazı özelliklerden mahrum kalmış oluruz. Service deki kadar esnek değil.
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByEmailEquals(String email, Pageable pageable);

    Page<ContactMessage> findBySubjectEquals(String subject, Pageable pageable);

    @Query("SELECT c FROM ContactMessage c WHERE FUNCTION('DATE', c.dateTime) BETWEEN ?1 and ?2") //ContactMessage tablosundan istediğim günler arasında tarih bilgileri geliyor.FUNCTION('DATE', c.dateTime) ile dateTime sütununda(javaca field'ında) ki, sadece DATE yani tarih kısmın getirecek. BETWEEN ?1 and ?2 ile de ->  1.parametre ile 2.parametre arasındaki dateleri contactMessage olarak bana getir.
    List<ContactMessage> findMessagesBetweenDates(LocalDate beginDate, LocalDate endDate);

}
