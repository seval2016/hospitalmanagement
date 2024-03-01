package com.project.security.jwt;

import com.project.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter { //JwtToken ile alakalı security katmanında gerekli olan methodları yada class'ları jwt package içerisinde oluşturuyoruz.AuthTokenFilter class'ının amacı JwtToken'leri JwtToken üzerinde kullanıcıları tanımlama işlemini design edebilmek için bize bir filtre lazım. AuthTokenFilter, JwtToken ile validate etme işlemlerini yapacak olan filter class'ıdır. AuthTokenFilter bir filter class'ı ve bunu securty'e bildirmek için OncePerRequestFilter'den extend ediyoruz.

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if(jwt !=null && jwtUtils.validateJwtToken(jwt) ){

                String userName = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                request.setAttribute("username", userName);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // bu UsernamePasswordAuthenticationToken nesnesinin ayrıntılarını ayarlar.
                // WebAuthenticationDetailsSource kullanarak, isteğin ayrıntılarını (örneğin, IP adresi,
                // kullanılan tarayıcı vb.) bu nesneye ekler. Bu bilgiler, kullanıcının oturum açma
                // isteğinin nereden geldiği ve hangi cihaz üzerinden yapıldığı gibi ayrıntıları içerir.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.error("Cannot set user authentication ", e);
        }
        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request){

        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){ //gelen stringin içi dolu olup olmadıgını kontrol edıyor
            return headerAuth.substring(7);
        }
        return null;
    }
}