package com.example.Backend.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public class CombinedUserDetailsService implements UserDetailsService {


    private final UserDetailsService adminService;
    private final UserDetailsService userService;

    public CombinedUserDetailsService(UserDetailsService adminService, UserDetailsService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return adminService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            System.out.println(username);
            return userService.loadUserByUsername(username);
        }
    }
}