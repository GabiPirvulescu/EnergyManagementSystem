package com.energymgmt.auth_service.services;

import com.energymgmt.auth_service.entities.UserCredentials;
import com.energymgmt.auth_service.repository.UserCredentialsRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;

    public CustomUserDetailsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials credentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new User(
                credentials.getUsername(),
                credentials.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + credentials.getRole()))
        );
    }
}