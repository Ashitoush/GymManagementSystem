package com.gymManagement.config;

import com.gymManagement.model.User;
import com.gymManagement.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User resultUser = this.userRepo.findByUserName(username);
        CustomUserDetails customUserDetails;
        if(resultUser == null) {
            throw new UsernameNotFoundException("User not find with " + username);
        } else {
            customUserDetails = new CustomUserDetails(resultUser);
        }

        return customUserDetails;

    }
}