package com.project.contactmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageRequest { //anonim sınıf

    // id'yi burada kullanmamıza gerek yok. Çünkü bu istek kullanıcıdan json dosya ile gelecek eğer kullanıcıdan gelmesini istiyorsak service katında muhakkak setlememiz lazım. Bu yüzden gerek yok

    //Datetime vermemize de gerek yok kullanıcı ileri tarihli bir mesaj atarsa gelcekten bize mesaj mı atacak ?

    //Bu sınıfta validation yapılması gerekıyor çünkü isteği bu sınıf karşılıyor bilgi olmazsa response verilemez.

    @NotNull(message = "Please enter name")
    @Size(min = 4, max = 16, message = "Your name should be at least 4 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "Your message must consist of the character .")
    private String name;

    @Email(message = "Please enter valid email")
    @NotNull(message = "Please enter your email")
    @Size(min = 4, max = 20, message = "Your email should be at least 5 chars")
    private String email;

    @NotNull(message = "Please enter subject")
    @Size(min = 4, max = 50, message = "Your subject should be at least 4 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "Your subject must consist of the character .")
    private String subject;

    @NotNull(message = "Please enter message")
    @Size(min = 4, max = 50, message = "Your message should be at least 4 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+",message = "Your message must consist of the character .")
    private String message ;

}