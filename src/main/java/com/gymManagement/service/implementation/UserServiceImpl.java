package com.gymManagement.service.implementation;

import com.gymManagement.exception.ResourceNotFoundException;
import com.gymManagement.model.Role;
import com.gymManagement.model.User;

import com.gymManagement.model.UserRole;
import com.gymManagement.repo.RoleRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

//    @Override
//    public User createUser(User user, List<UserRole> userRoleList) throws Exception {
//
//        User localUser = this.userRepo.findByUserName(user.getUserName());
//
//        if (localUser != null) {
//            System.out.println("User with that username already exists!!!");
//            throw new Exception("User already exists!!!");
//        } else {
//            for (UserRole userRole : userRoleList) {
//                this.roleRepo.save(userRole.getRole());
//                user.setUserRoles(userRoleList);
//                user.setPassword(passwordEncoder.encode(user.getPassword()));
//                userRole.setUser(user);
//                localUser = this.userRepo.save(user);
//            }
//        }
//        return localUser;
//    }

//    @Override
//    public User createUser(User user, List<Role> roles) throws Exception {
//        List<UserRole> userRoleList = new ArrayList<>();
//        UserRole userRole = new UserRole();
//
//        User user1 = this.userRepo.findByUserName(user.getUserName());
//
//        if (user1 != null) {
//            throw new Exception("User already exist with the given Username: " + user.getEmail());
//        } else {
//            for (Role eachRole : roles) {
//                Role role = this.roleRepo.findByRoleName(eachRole.getRoleName());
//
//                if (role == null) {
//                    throw new Exception("User did not provide valid role for registration!!!");
//                } else {
//                    userRole.setUser(user);
//                    userRole.setRole(role);
//                    userRoleList.add(userRole);
//                    role.setUserRoles(userRoleList);
//                    user.setUserRoles(userRoleList);
//
//                    user1 = this.userRepo.save(user);
//                }
//            }
//        }
//        return user1;
//    }

    @Override
    public User createUser(User user, List<Role> roles) throws Exception {
        List<UserRole> userRoleList = new ArrayList<>();

        User user1 = this.userRepo.findByUserName(user.getUserName());

        if (user1 != null) {
            throw new Exception("User already exist with the given Username: " + user.getEmail());
        } else {
            for (Role eachRole : roles) {
                Role role = this.roleRepo.findByRoleName(eachRole.getRoleName());

                if (role == null) {
                    throw new Exception("User did not provide valid role for registration!!!");
                } else {
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    userRoleList.add(userRole);
                }
            }
            user.setUserRoles(userRoleList);
            user1 = this.userRepo.save(user);
        }
        return user1;
    }


    @Override
    public User getUserByUserName(String username) {
        User resultUser = this.userRepo.findByUserName(username);
        if (resultUser != null) {
            return resultUser;
        } else {
            throw new ResourceNotFoundException("User", "userName", username);
        }
    }

    @Override
    public String deleteUser(Long id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "userId" + id, ""));
        String message = null;
        if (user != null) {
            user.getUserRoles().add(null);
            user.setUserRoles(null);
            this.userRepo.delete(user);
            message = "User with userId " + id + " deleted successfully";
        }
        return message;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User searchedUser = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("username", "userId " + userId, null));
        if (searchedUser != null) {
            searchedUser.setUserName(user.getUserName());
            searchedUser.setPassword(user.getPassword());
            searchedUser.setEmail(user.getEmail());
            searchedUser.setFirstName(user.getFirstName());
            searchedUser.setLastName(user.getLastName());
            searchedUser.setPhoneNumber(user.getPhoneNumber());
            this.userRepo.save(searchedUser);
        }
        return searchedUser;
    }
}
