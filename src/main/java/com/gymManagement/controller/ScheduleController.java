package com.gymManagement.controller;

import com.gymManagement.dto.ScheduleDto;
import com.gymManagement.helper.ApiResponse;
import com.gymManagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_schedule')")
    Map<String, Object> create(@RequestBody ScheduleDto scheduleDto) {
        Map<String, Object> message = new HashMap<>();
        ScheduleDto scheduleDto1 = this.scheduleService.createSchedule(scheduleDto);
        if (scheduleDto1 != null) {
            message.put("status", 200);
            message.put("message", "Schedule created");
            message.put("data", scheduleDto1);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while creating schedule");
        }
        return message;
    }

    @PutMapping("/update/{scheduleId}")
    @PreAuthorize("hasAuthority('update_schedule')")
    Map<String, Object> update(@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleDto scheduleDto) {
        Map<String, Object> message = new HashMap<>();
        ScheduleDto scheduleDto1 = this.scheduleService.updateSchedule(scheduleId, scheduleDto);

        if (scheduleDto1 != null) {
            message.put("status", 200);
            message.put("message", "Schedule updated");
            message.put("data", scheduleDto1);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while updating schedule");
        }
        return message;
    }

    @GetMapping("/getAllSchedule")
    @PreAuthorize("hasAuthority('view_schedule')")
    Map<String, Object> getAllSchedule() {
        Map<String, Object> message = new HashMap<>();
        List<ScheduleDto> scheduleDto1 = this.scheduleService.getAllSchedule();

        if (scheduleDto1 != null) {
            message.put("status", 200);
            message.put("message", "All Schedule retrieved");
            message.put("data", scheduleDto1);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving schedule");
        }
        return message;
    }

    @GetMapping("/getScheduleById/{scheduleId}")
    @PreAuthorize("hasAuthority('view_schedule')")
    Map<String, Object> getScheduleById(@PathVariable("scheduleId")Long scheduleId) {
        Map<String, Object> message = new HashMap<>();
        ScheduleDto scheduleDto1 = this.scheduleService.getScheduleById(scheduleId);

        if (scheduleDto1 != null) {
            message.put("status", 200);
            message.put("message", "Schedule retrieved");
            message.put("data", scheduleDto1);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving schedule");
        }
        return message;
    }

    @PostMapping("/addUserToSchedule/{scheduleId}")
    @PreAuthorize("hasAuthority('add_user_to_schedule')")
    Map<String, Object> create(@PathVariable("scheduleId") Long scheduleId, Principal principal) {
        Map<String, Object> message = new HashMap<>();
        ScheduleDto scheduleDto1 = this.scheduleService.addUserToSchedule(scheduleId, principal);

        if (scheduleDto1 != null) {
            message.put("status", 200);
            message.put("message", "User add to schedule");
            message.put("data", scheduleDto1);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while adding user to schedule");
        }
        return message;
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('delete_schedule')")
    Map<String, Object> deleteSchedule(@PathVariable("userId") Long userId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        String retrievedMessage = this.scheduleService.deleteSchedule(userId);
        if(!retrievedMessage.isEmpty()) {
            message.put("status", 200);
            message.put("message", retrievedMessage);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while deleting schedule");
        }
        return message;
    }
}
