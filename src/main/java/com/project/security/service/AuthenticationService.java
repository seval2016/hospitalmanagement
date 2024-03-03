package com.project.service;

import com.project.entity.concretes.user.User;
import com.project.exception.BadRequestException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        //!!! Gelen requestin icinden kullanici adi ve parola bilgisi aliniyor
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // !!! authenticationManager uzerinden kullaniciyi valide ediyoruz
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        // !!! valide edilen kullanici Context e atiliyor
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // !!! JWT token olusturuluyor
        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);
        // !!! login islemini gerceklestirilen kullaniciya ulasiliyor
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // !!!  Response olarak login islemini yapan kullaniciyi donecegiz gerekli fieldlar setleniyor

        // !!! GrantedAuthority turundeki role yapisini String turune ceviriliyor
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        //!!! bir kullanicinin birden fazla rolu olmayacagi icin ilk indexli elemani aliyoruz
        Optional<String> role = roles.stream().findFirst();

        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.username(userDetails.getUsername());
        authResponse.token(token.substring(7));
        authResponse.name(userDetails.getName());
        authResponse.ssn(userDetails.getSsn());
        role.ifPresent(authResponse::role);

        return  ResponseEntity.ok(authResponse.build());
    }

    public UserResponse findByUsername(String username) {
/*       User user = userRepository.findByUsernameEquals(username);
       return userMapper.mapUserToUserResponse(user);*/
        return userMapper.mapUserToUserResponse(userRepository.findByUsernameEquals(username));
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest,
                               HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");
        User user = userRepository.findByUsernameEquals(userName);
        //!!! built_in kontrolu ??
        if(Boolean.TRUE.equals(user.getBuilt_in())){
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
        //!!! Eski sifre bilgisi dogru mu ??
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())){
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED);
        }
        // !!! yeni sifre hashlenerek Kaydediliyor
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
}