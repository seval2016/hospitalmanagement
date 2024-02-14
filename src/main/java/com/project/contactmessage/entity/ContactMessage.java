package com.project.contactmessage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data // Data annotation'un arka planında EqualsAndHashCode diye bişey var. Bu şu anlama geliyor. Elimizde iki ayrı nesne var bunlar ayrı nesneler fakat içerisindeki bilgiler aynı. Böyle bir durumda EqualsAndHashCode devreye girer. Yazığımız nesnenin değişkenlerinin valuelerine göre bir hashcode çıkartır. İki nesnedeki değişkenler aynı olduğu için ıkı hashcode aynı olacaktır. (Java da bu doğru değil çünkü bellekte tuttukları yerler farklı.) Dolayısıyla ikisi de içerik olarak aynı nesne diyebiliriz ve ıkı nesneyi kontrol etmemiz gerektiğinde tek tek değişkenleri kontrol etmek yerine EqualsAndHashCode ile hızlı bir şekilde bulabiliriz. Fakat Data annotation'u  kullanmak çok tavsiye edilmez Neden ? Bu annotation'u kullandığımız sınıfta her nesne oluşturduğumuzda bu hash code algoritmasına sokuyor bu da performans kaybına yol açıyor ve projeyi yavaşlatıyor. Eğer kıyaslama tarzı bir lojik yapı değilse kullanılması uygun değilse annoattion uyarı verıyor boş yere kullanma diye !
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // farklı argumanlara (parametre) sahip const oluşturabilmek için bu annotation kullanılır.
//toBuilder = true -> constractor'a istediğimiz parametreyi sonradan ekleyebilmek için kullanılıyor
@Entity
public class ContactMessage implements Serializable { //Serializable java classlarını internet ortamında bir yerden bir yere gönderiyorsak serialize edilmesi gerekiyor. Bu interface ile verilerin tür dönüşümü sağlanır. SpringBoot uygulamalarında jkson  kütüphanelerinde olduğu için tekrar implement etmeye gerek yok.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;


    @NotNull
    private String email;

    @NotNull
    private String subject;

    @NotNull
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime dateTime;

}