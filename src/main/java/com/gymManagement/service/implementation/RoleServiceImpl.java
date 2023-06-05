package com.gymManagement.service.implementation;

import com.gymManagement.model.Role;
import com.gymManagement.repo.RoleRepo;
import com.gymManagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role createNewRole(Role role) {
        return roleRepo.save(role);
    }
}
