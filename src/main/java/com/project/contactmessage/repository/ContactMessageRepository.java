package com.project.contactmessage.repository;

import com.project.contactmessage.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT c FROM ContactMessage c WHERE " +
            "(EXTRACT(HOUR FROM c.dateTime) BETWEEN :startHour AND :endHour) AND " +
            "(EXTRACT(HOUR FROM c.dateTime) != :startHour OR EXTRACT(MINUTE FROM c.dateTime) >= :startMinute) AND " + // baslama dakikasina gore kontrol
            "(EXTRACT(HOUR FROM c.dateTime) != :endHour OR EXTRACT(MINUTE FROM c.dateTime) <= :endMinute)") // bitis dakikasina gore kontrol
    List<ContactMessage> findMessagesBetweenTimes(@Param("startHour") int startHour,
                                                  @Param("startMinute") int startMinute,
                                                  @Param("endHour") int endHour,
                                                  @Param("endMinute") int endMinute); //@Param ile service katından gelen endMinute değişkenini yukarıdaki jpql'de kullan diyoruz.

    	/*
        EXTRACT() ile FUNCTION() aynı işlemleri yapıyor.
        EXTRACT(HOUR FROM c.dateTime) < = > FUNCTION('DATE', c.dateTime)

    	1.condition ->  db'de dateTime verileri içerisinde startHour ile endHour arasında bulunan saat bilgisini al  ve acaba gelen saat bu saatler arasında mı diye kontrol et ve veri varsa al
    	 - ve -
    	2.conditon -> Eğer başlama saatine eşit değil ise veya elimdeki başlama saati dakikası benim başlama saatimden büyük yada eşit ise
        - ve -
        3.conditon -> Eğer bitiş saatine eşit değil ise veya elimdeki bitiş saati dakikası benim bitiş saatimden küçük yada eşit ise verileri bana getir.


		Yukardaki Query icin ornek senaryo :
		Db deki var olan ContactMessage  saati 09:30
		Gelen sorguda gelen parametreler --> 09:15 / 17:15
		Ilk AND --> TRUE
		ikinci AND --> False OR True
		ucuncu AND --> True OR False
	 */ // Query aciklama


}