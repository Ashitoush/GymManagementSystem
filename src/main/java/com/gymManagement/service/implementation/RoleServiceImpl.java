package com.gymManagement.service.implementation;

import com.gymManagement.dto.AuthorityDto;
import com.gymManagement.dto.RoleDto;
import com.gymManagement.model.Authority;
import com.gymManagement.model.Role;
import com.gymManagement.repo.AuthorityRepo;
import com.gymManagement.repo.RoleRepo;
import com.gymManagement.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AuthorityRepo authorityRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role resultRole;
        Role role = new Role();
        role.setRoleName(roleDto.getName());
        role.setAuthorities(roleDto.getAuthoritySet().stream().map(a -> {
            Authority authority = authorityRepo.findByAuthorityName(a.getName());
            if(authority == null) {
                authority = authorityRepo.save(new Authority(a.getName()));
            }
            return authority;
        }).collect(Collectors.toSet()));
        resultRole = roleRepo.save(role);
        RoleDto roleDto1 = new RoleDto();
        roleDto1.setName(resultRole.getRoleName());
        roleDto1.setAuthoritySet(role.getAuthorities().stream().map(authority -> this.modelMapper.map(authority, AuthorityDto.class)).collect(Collectors.toSet()));

        return roleDto1;
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, Long roleId) throws Exception {
        Role role = this.roleRepo.findById(roleId).get();
        Role retrievedRole = new Role();
        if(roleDto.getName() != null) {
            retrievedRole = this.roleRepo.findByRoleName(roleDto.getName());
            if(retrievedRole.equals(null)) {
                role.setRoleName(roleDto.getName());
            }
        }
        Set<AuthorityDto> authoritySet = roleDto.getAuthoritySet();

        if(!authoritySet.isEmpty()) {
            role.setAuthorities(roleDto.getAuthoritySet().stream().map(a -> {
                Authority authority = authorityRepo.findByAuthorityName(a.getName());
                if(authority != null) {
                    authority = authorityRepo.save(new Authority(a.getName()));

                } else {
                    try {
                        throw new Exception("Authority with the given name already defined!!!");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return authority;
            }).collect(Collectors.toSet()));
        }

        Role resultRole = roleRepo.save(role);
        RoleDto roleDto1 = new RoleDto();
        roleDto1.setName(resultRole.getRoleName());
        roleDto1.setAuthoritySet(resultRole.getAuthorities().stream().map(authority -> this.modelMapper.map(authority, AuthorityDto.class)).collect(Collectors.toSet()));

        return roleDto1;
    }
}
