package com.example.task_manager.service;

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
        if ("user".equals(username)) { // Replace this with your actual user lookup logic
            return new User(
                    "user", // username
                    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password"), // encoded password
                    new ArrayList<>() // roles/authorities
            );
        }
        throw new UsernameNotFoundException("User not found");
    }
}
