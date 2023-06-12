package com.gymManagement.security;

import com.gymManagement.dto.UserDto;
import com.gymManagement.model.User;
import com.gymManagement.repo.RoleRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found"+username);
        }
        UserDto userDto=this.userService.getUserDto(user);
        UserPrincipal userPrincipal=new UserPrincipal(userDto, roleRepo);

        return userPrincipal;
    }
}