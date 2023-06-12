package com.gymManagement.service;

import com.gymManagement.dto.UserDto;
import com.gymManagement.model.Role;
import com.gymManagement.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto create(UserDto userDto) throws Exception;

    UserDto createUser(UserDto userDto) throws Exception;
    UserDto createStaff(UserDto userDto) throws Exception;
    UserDto updateUser(Long userId, UserDto userDto) throws Exception;
    String deleteUser(Long userId);
    UserDto getUserByEmail(String email);
    UserDto getUserDto(User user);
    List<UserDto> getAllUser();
    List<UserDto> getAllStaff();
    Long getTotalUserCount();
    UserDto getUserDetail(Principal principal);
}