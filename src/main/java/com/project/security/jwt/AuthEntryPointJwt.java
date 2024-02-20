package com.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    //  kimlik doğrulama hatası olduğunda otomatik olarak çalışacak methodu override ediyoruz..
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // hata mesajı bir log dosyasına veya konsola kaydedilir.
        LOGGER.error("Unauthorized error : {}", authException.getMessage());
        // response turu JSON olacak
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // ve HTTP Status Cod da 401, UnAuthorized olacagini setliyorum
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String,Object> body = new HashMap<>();
        body.put("status",HttpServletResponse.SC_UNAUTHORIZED );
        body.put("error", "Unauthorized");
        body.put("message",authException.getMessage());
        body.put("path",request.getServletPath());

        // olusturdugumuz Map yapiyi response icine gonderiyoruz
        final ObjectMapper objectMapper = new ObjectMapper();// ObjectMapper --> Java nesnelerini JSON'a dönüştürmek veya
        // JSON'u Java nesnelerine dönüştürmek için gerekli
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}