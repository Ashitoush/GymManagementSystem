package com.gymManagement.controller;

import com.gymManagement.dto.RoleDto;
import com.gymManagement.model.Role;
import com.gymManagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @PostMapping("/createNewRole")
    public RoleDto createNewRole(@RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }
    @PutMapping("/updateRole/{roleId}")
    public RoleDto updateRole(@PathVariable("roleId") Long roleId, @RequestBody RoleDto roleDto) throws Exception {
        return roleService.updateRole(roleDto, roleId);
    }

}