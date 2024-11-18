package com.example.main.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user".equals(username)) {
            return new User(
                    "user", 
                    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password"),
                    new ArrayList<>() 
            );
        }
        throw new UsernameNotFoundException("User not found");
    }
}
