package com.project.contactmessage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageResponse {
   //Bu bir response nesnesi ve üzerinde değişiklik yaptığımız yada kalıcı hale getirdiğimiz nesneleri client'e döndürür.

   // Böyle bir response nesnesi neden yapılıyor! Çünkü front end tarafından tüm bilgiler talep edilebilir. Eğer gerek olmasaydı "mesajınız kaydedildi" dönmek yeterli olabilirdi.

    //client için id bilgisinin hiç bir önemi yok bu yüzden istersem id bilgisini yazmadan gönderebilirim.

   //Burada validation kullanmaya gerek yoktur çünkü bu bilgileir zaten db den geldiği için pojo class dan db ye kaydedilirken validation yapılıyor ıkıncı bir kez yapmaya gerek yoktur performans kaybına neden olur.

        private String name;
        private String email;
        private String subject;
        private String message;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime dateTime;
}
