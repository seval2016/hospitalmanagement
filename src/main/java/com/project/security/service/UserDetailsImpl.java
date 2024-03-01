package com.project.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails { //UserDetailsImpl class'ı security'nin anlayacağı user class. yani bu class ile security bizim user'larımızı anlar. Çünkü security user bilmez, user yerine userdetails'i bilir security roller'i bilmez yerine grantedautherity bilir. Özetle UserDetailsImpl sınıf securtiy'nin entity'si gibi düşünebiliriz. Eğer direkt user kullansak user da bulunan tüm veriler gelir ki bu gereksiz olur. Bize lazım olan id,username,password dir. Bu üç veri benim için yeterli diyebilirim. Bu yüzden bu class yazılır.

    private Long id;
    private String username;
    private String name;
    private Boolean isChiefDoctor;
    @JsonIgnore
    private String password;
    private String ssn;
    private Collection<? extends GrantedAuthority> authorities;//GrantedAuthority sınıfından extend edilen herhangi bir sınıf(yani rol) buraya gelebilir.

    public UserDetailsImpl(Long id, String username, String name, Boolean isChiefDoctor,
                           String password, String role , String ssn) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isChiefDoctor = isChiefDoctor;
        this.password = password;
        this.ssn = ssn;

        //String role'ü security'nin anlayacağı GrantedAuthority data türüne çevirmiş olduk
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Elimize iki tane security'nin anlayacağı user yani security'nin anlayacağı şekilde userdetails geldiğini düşünelim. Acaba bu iki userdetails aynı user mı?
    public boolean equals(Object o){
        if(this == o){ // bu methodu cagiran nesne ile Parametrede gelen "o" isimli
            // nesneyi karsilastirir, ama Java bu nesnelerin bellekteki referanslarını (adreslerini) karşılaştırır.
            return true;
        }
        //parametre ile gelen nesnenin userDetailsImpl turunde olup olmadığını kontrol ediyoruz
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        //UserDetailsImpl sınıfından olduğu doğrulanan nesnenin id özelliğinin
        // mevcut nesnenin id özelliği ile eşit olup olmadığını kontrol eder.
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.getId()); // id ile kiyaslama
    }


}