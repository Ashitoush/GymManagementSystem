package com.gymManagement.security;

import com.gymManagement.dto.UserDto;
import com.gymManagement.model.Role;
import com.gymManagement.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import java.util.stream.Collectors;


public class UserPrincipal implements UserDetails {
    @Autowired
    private UserDto userDto;
    @Autowired
    private RoleRepo roleRepo;

    public UserPrincipal(UserDto userDto, RoleRepo roleRepo){
        super();
        this.userDto=userDto;
        this.roleRepo=roleRepo;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role retrievedRole=roleRepo.findByRoleName(userDto.getRoleName());
        List<Role> roles=new ArrayList<>();
        roles.add(retrievedRole);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role eachRole:roles
        ) {
            authorities.addAll(eachRole.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthorityName()))
                    .collect(Collectors.toSet()));

        }


        return authorities;
    }

    public String getRole() {
        return userDto.getRoleName();
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}