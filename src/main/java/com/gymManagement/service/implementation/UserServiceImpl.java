package com.gymManagement.service.implementation;

import com.gymManagement.dto.UserDto;
import com.gymManagement.exception.ResourceNotFoundException;
import com.gymManagement.helper.AesEncryptionUtils;
import com.gymManagement.model.Role;
import com.gymManagement.model.User;

import com.gymManagement.repo.RoleRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto create(UserDto userDto) throws Exception {
        Map<String, Object> message = new HashMap<>();
        if(checkIfUserNameExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Name Already Exists.");
        }
        if(checkIfEmailExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists.");
        }

        Role retrievedRole = roleRepo.findByRoleName(userDto.getRoleName().toUpperCase());
        User user;
        if (retrievedRole != null) {
            user = setUserDetails(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
            user = userRepo.save(user);
        } else {
            throw new Exception("Invalid Role!!!");
        }
        return getUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) throws Exception {
        if(checkIfUserNameExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Name Already Exists.");
        }
        if(checkIfEmailExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists.");
        }

        Role retrievedRole = roleRepo.findByRoleName("USER");
        User user;
        if (retrievedRole != null) {
            user = setUserDetails(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
            user = userRepo.save(user);
        } else {
            throw new Exception("Invalid Role!!!");
        }
        return getUserDto(user);
    }

    @Override
    public UserDto createStaff(UserDto userDto) throws Exception {
        if(checkIfUserNameExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Name Already Exists.");
        }
        if(checkIfEmailExists(userDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists.");
        }

        Role retrievedRole = roleRepo.findByRoleName("STAFF");
        User user;
        if (retrievedRole != null) {
            user = setUserDetails(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
            user = userRepo.save(user);
        } else {
            throw new Exception("Invalid Role!!!");
        }
        return getUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User retrievedUser = this.userRepo.findByEmail(email);
        if (retrievedUser == null) {
            throw new ResourceNotFoundException("User", "Email " + email, null);
        }
        return this.getUserDto(retrievedUser);
    }

    @Override
    public UserDto getUserDetail(Principal principal) {
        User user = this.userRepo.findByEmail(principal.getName());
        return this.getUserDto(user);
    }

    @Override
    public String deleteUser(Long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userId " + userId, ""));
        String message = "";
        if (user != null) {
            user.getUserRoles().clear();
            this.userRepo.delete(user);
            message = "User with userId " + userId + " deleted successfully";
        }
        return message;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws Exception {
        User retrievedUser = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("username", "userId " + userId, null));

        if (retrievedUser == null) {
            throw new Exception("User Not Found with User Id: " + userId, null);
        } else {
            retrievedUser.setUserName(userDto.getUserName());
//            retrievedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            retrievedUser.setPassword(userDto.getPassword());
            retrievedUser.setEmail(userDto.getEmail());
            retrievedUser.setFirstName(userDto.getFirstName());
            retrievedUser.setMiddleName(userDto.getMiddleName());
            retrievedUser.setLastName(userDto.getLastName());
            retrievedUser.setPhoneNumber(userDto.getPhoneNumber());
            retrievedUser.setGender(userDto.getGender());
            retrievedUser.setAddress(userDto.getAddress());
            retrievedUser.setDob(userDto.getDob());
            retrievedUser = this.userRepo.save(retrievedUser);
        }
        return this.getUserDto(retrievedUser);
    }

    public Boolean checkIfUserNameExists(UserDto userDto) {
        return StringUtils.hasText(userDto.getUserName()) && userRepo.findByUserName(userDto.getUserName().toLowerCase())!=null;
    }
    public Boolean checkIfEmailExists(UserDto userDto) {
        return StringUtils.hasText(userDto.getEmail()) && userRepo.findByEmail(userDto.getEmail().toLowerCase())!=null;
    }
    public UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setUserName(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setGender(user.getGender());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAddress(user.getAddress());
        userDto.setDob(user.getDob());

        for (Role role : user.getUserRoles()) {
            userDto.setRoleName(role.getRoleName());
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepo.findAll();
        List<UserDto> userList = new ArrayList<>();
        for (User user : users) {
            if (user.getUserRoles().stream().anyMatch(role -> role.getRoleName().equals("USER"))) {
                userList.add(getUserDto(user));
            }
        }
        return userList;
    }



    @Override
    public List<UserDto> getAllStaff() {
        List<User> users = userRepo.findAll();
        List<UserDto> userList = new ArrayList<>();
        for (User user : users) {
            if (user.getUserRoles().stream().anyMatch(role -> role.getRoleName().equals("STAFF"))) {
                userList.add(getUserDto(user));
            }
        }
        return userList;
    }

    public User setUserDetails(UserDto userDto) throws Exception {
        User user = new User();

        user.setEmail(userDto.getEmail().toLowerCase());
        user.setUserName(userDto.getUserName());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setGender(userDto.getGender());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setDob(userDto.getDob());

        return user;
    }

    @Override
    public Long getTotalUserCount() {
        List<User> users = userRepo.findAll();
        Long userCount = 0L;

        for (User user : users) {
            for (Role role : user.getUserRoles()) {
                if (role.getRoleName().equalsIgnoreCase("USER")) {
                    userCount++;
                    break;
                }
            }
        }

        return userCount;
    }
}