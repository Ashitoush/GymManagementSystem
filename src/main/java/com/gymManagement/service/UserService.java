package com.gymManagement.service;

import com.gymManagement.model.Role;
import com.gymManagement.model.User;
import com.gymManagement.model.UserRole;

import java.util.List;

public interface UserService {

//    User createUser(User user, List<UserRole> userRoleList) throws Exception;

    User createUser(User user, List<Role> roles) throws Exception;
    User getUserByUserName(String username);
    String deleteUser(Long id);

    User updateUser(Long userId,User user);
}