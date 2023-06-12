package com.gymManagement.service;

import com.gymManagement.dto.RoleDto;
import com.gymManagement.model.Role;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto updateRole(RoleDto roleDto, Long roleId) throws Exception;
}
