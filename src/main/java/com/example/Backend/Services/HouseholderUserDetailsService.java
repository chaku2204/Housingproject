package com.example.Backend.Services;

import com.example.Backend.Model.Householder;
import com.example.Backend.Repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class HouseholderUserDetailsService implements UserDetailsService {


    @Autowired
    private HouseRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Householder user = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );

        return new User(user.getEmail(), user.getPassword(),
                Collections.singleton(() -> "ROLE_" + user.getRole().name()));
    }
}
