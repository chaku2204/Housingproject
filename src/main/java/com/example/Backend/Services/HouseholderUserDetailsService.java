package com.example.Backend.Services;

import com.example.Backend.Model.Householder;
import com.example.Backend.Repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HouseholderUserDetailsService implements UserDetailsService {


    @Autowired
    private HouseRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Householder user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println(username+"user"+user);
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // Assign ROLE_USER
                .build();
    }
}
