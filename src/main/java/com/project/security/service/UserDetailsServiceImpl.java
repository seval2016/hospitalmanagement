package com.project.security.service;

import com.project.entity.concretes.user.User;
import com.project.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {// Securtiy sadece kendisine ait katmanları bilir user, user controller veya user service'i bilmez. Onun anlayacağı bir service yazmamız gerekir. UserDetailsServiceImpl class'ı da security'nin anlayacağı service class'ı dır. Bunu security'e söyleyebilmek için UserDetailsService classını implement etmeliyiz.

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameEquals(username);

        if(user !=null){ //Eğer user null'a eşit değilse UserDetailsImpl türüne çevireceğiz.
            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    false,
                    user.getPassword(),
                    user.getUserRole().getRoleType().name(),
                    user.getSsn()
            );
        }
        throw new UsernameNotFoundException("User '" + username + "' not found"); // User 'Seval' nt found
    }
}