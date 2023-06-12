package com.gymManagement.controller;

import com.gymManagement.dto.UserDto;
import com.gymManagement.helper.AesEncryptionUtils;
import com.gymManagement.helper.ApiResponse;
import com.gymManagement.helper.JwtHelper;
import com.gymManagement.model.User;
import com.gymManagement.repo.RoleRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.security.CustomUserDetailService;
import com.gymManagement.security.UserPrincipal;
import com.gymManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AesEncryptionUtils aesEncryptionUtils;

    private static final String secretKey = "aesEncryptionKey";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/createAdmin")
    Map<String, Object> create(@RequestBody UserDto userDto) throws Exception {
        Map<String, Object> message = new HashMap<>();
        userDto.setPassword(aesEncryptionUtils.encrypt(userDto.getPassword(), secretKey));
        UserDto resultUser = this.userService.create(userDto);
        if(resultUser != null) {
            message.put("status", 200);
            message.put("message", "Admin Created Successfully");
            message.put("data", resultUser);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot create admin");
        }
        return message;
    }

    @PostMapping("/create")
    Map<String, Object> createUser(@RequestBody UserDto userDto) throws Exception {
        Map<String, Object> message = new HashMap<>();
        userDto.setPassword(aesEncryptionUtils.encrypt(userDto.getPassword(), secretKey));
        UserDto resultUser = this.userService.createUser(userDto);
        if(resultUser != null) {
            message.put("status", 200);
            message.put("message", "User Created Successfully");
            message.put("data", resultUser);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot create user");
        }
        return message;
    }

    @PostMapping("/createStaff")
    @PreAuthorize("hasAuthority('create_staff')")
    Map<String, Object> createStaff(@RequestBody UserDto userDto) throws Exception {
        Map<String, Object> message = new HashMap<>();
        userDto.setPassword(aesEncryptionUtils.encrypt(userDto.getPassword(), secretKey));
        UserDto resultUser = this.userService.createStaff(userDto);
        if(resultUser != null) {
            message.put("status", 200);
            message.put("message", "Staff Created Successfully");
            message.put("data", resultUser);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot create staff");
        }
        return message;
    }
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('delete_user')")
    Map<String, String> deleteUser(@PathVariable("userId") Long userId) throws Exception {
        Map<String, String> message = new HashMap<>();
        String retrievedMessage = this.userService.deleteUser(userId);
        if(!message.isEmpty()) {
            message.put("status", "200");
            message.put("message", retrievedMessage);
        } else {
            message.clear();
            message.put("status", "500");
            message.put("message", "Something went wrong while deleting user");
        }
        return message;
    }

    @PutMapping("/update")
    Map<String, Object> updateUser(Principal principal, @RequestBody UserDto userDto) throws Exception {
        Map<String, Object> message = new HashMap<>();
        User user = userRepo.findByEmail(principal.getName());
        UserDto updatedUserDto = this.userService.updateUser(user.getId(), userDto);
        if(updatedUserDto != null) {
            message.put("status", 200);
            message.put("message", "user updated");
            message.put("data", updatedUserDto);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Something Went Wrong while updating");
        }
        return message;
    }

    @GetMapping("/getUser")
    Map<String, Object> getUser(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        List<User> users = this.userRepo.findAll();
        message.put("data", users);
        message.put("fullName", fullName);
        return message;
    }

    @GetMapping("/getAllUser")
//    @PreAuthorize("hasAuthority('view_all_user')")
    Map<String, Object> getAllUser(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        List<UserDto> userDto = this.userService.getAllUser();
        if(!userDto.isEmpty()) {
            message.put("status", 200);
            message.put("message", "All user retrieved");
            message.put("data", userDto);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot access user");
        }
        message.put("fullName", fullName);
        return message;
    }

    @GetMapping("/getAllStaff")
    @PreAuthorize("hasAuthority('view_all_staff')")
    Map<String, Object> getAllStaff(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        List<UserDto> userDto = this.userService.getAllStaff();
        if(!userDto.isEmpty()) {
            message.put("status", 200);
            message.put("message", "All staff retrieved");
            message.put("data", userDto);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot access staff");
        }
        message.put("fullName", fullName);
        return message;
    }

    @GetMapping("/totalUserCount")
    @PreAuthorize("hasAuthority('view_total_user_count')")
    Map<String, Object> getTotalUserCount() {
        Map<String, Object> message = new HashMap<>();
        Long totalUserCount = userService.getTotalUserCount();
        if(totalUserCount >= 0) {
            message.put("status", 200);
            message.put("message", "Total number of user");
            message.put("data", totalUserCount);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot access total number of user");
        }
        return message;
    }

    @GetMapping("/userProfile")
    Map<String, Object> getUserDetail(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        UserDto userDto = this.userService.getUserDetail(principal);
        userDto.setFullName(userDto.getUserName() + " " + userDto.getLastName());
        message.put("status", 200);
        message.put("data", userDto);
        return message;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userDto) throws Exception {
        Map<String, String> message = new HashMap<>();
        String userName= userDto.getEmail();
//        String password=userDto.getPassword();
        String password = aesEncryptionUtils.encrypt(userDto.getPassword(), secretKey);
        UserDetails userDetails=this.customUserDetailService.loadUserByUsername(userName);
        this.authenticate(userName,password,userDetails.getAuthorities());
        UserDto user = this.userService.getUserByEmail(userDto.getEmail());
        if(user != null) {
            user.setPassword(null);
            String jwtToken = this.jwtHelper.generateToken(new UserPrincipal(user, roleRepo));
            String jwt="Bearer " + jwtToken;
            message.put("status", "200");
            message.put("JwtToken", jwt);
            message.put("Role", user.getRoleName());
            message.put("message", "Logged In Successfully.");
            return ResponseEntity.ok(message);
        }else{
            throw new Exception("Wrong credentials");
        }
    }

    private void authenticate(String userName, String password, Collection<? extends GrantedAuthority> authorities) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userName,password,authorities);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (DisabledException e){
            throw new Exception("user is disabled!!!");
        }
    }

}