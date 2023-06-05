package com.gymManagement.controller;

import com.gymManagement.helper.JwtAuthRequest;
import com.gymManagement.model.Role;
import com.gymManagement.model.User;
import com.gymManagement.service.JwtService;
import com.gymManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(jwtAuthRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid User Request!!!");
        }
    }

    //    @PostMapping("/createUser")
//    public User createNewUser(@RequestBody User user, @RequestBody List<Role> roles) throws Exception {
//        return userService.createUser(user, roles);
//    }
    @PostMapping("/create")
    public User createUser(@RequestBody User user) throws Exception {
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();


        role.setRoleName("ADMIN");
        roles.add(role);


        User resultedUser;


        //user.setRoleList(roleList);
        resultedUser = this.userService.createUser(user, roles);

        return resultedUser;

    }

    @GetMapping("/welcome")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String welcomeAdmin() {
        return "Welcome Admin!";
    }

//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/create")
//    ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
//        List<UserRole> userRoleList=new ArrayList<>();
//        UserRole userRole=new UserRole();
//        Role role=new Role();
//        role.setRoleName("Admin");
////        role.setRoleId(89L);
//        role.setUserRoles(userRoleList);
//        userRole.setUser(user);
//        userRole.setRole(role);
//
//        userRoleList.add(userRole);
//
//        User resultUser=this.userService.createUser(user,userRoleList);
//        return new ResponseEntity<>(resultUser, HttpStatusCode.valueOf(200));
//    }
//
//    @GetMapping("/read/{userName}")
//    ResponseEntity<User> getUserByUserName(@PathVariable("userName")String userName){
//        User user=this.userService.getUserByUserName(userName);
//        return new ResponseEntity<>(user,HttpStatusCode.valueOf(200));
//
//
//    }
//    @DeleteMapping("/delete")
//    ResponseEntity<ApiResponse> deleteUser(@RequestParam("userId")Long userId ){
//        String message=this.userService.deleteUser(userId);
//        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
//
//
//    }
//
//    @PutMapping("/update/{userId}")
//    ResponseEntity<User> updateUser(@PathVariable("userId")Long userId,@RequestBody User user){
//        User updatedUser=this.userService.updateUser(userId,user);
//        return new ResponseEntity<>(updatedUser,HttpStatusCode.valueOf(200));
//
//    }

}