package com.gymManagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String email;
    private String fullName;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private LocalDate dob;
    private String gender;
    private String phoneNumber;
    private String address;
    private String roleName;
    private List<Long> payments;
    private List<Long> userSchedules;
}
