package com.gymManagement.controller;

import com.gymManagement.dto.BodyStatsDto;
import com.gymManagement.model.User;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.BodyStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bodyStats")
public class BodyStatsController {

    @Autowired
    private BodyStatsService bodyStatsService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('create_bodyStats')")
    public Map<String, Object> createBodyStats(@RequestBody BodyStatsDto bodyStatsDto, Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByUserName(principal.getName());
        Long userId = loggedInUser.getId();
        bodyStatsDto.setUserId(userId);
        BodyStatsDto createdBodyStats = bodyStatsService.createBodyStats(bodyStatsDto);
        if(createdBodyStats != null) {
            message.put("status", 200);
            message.put("message", "body Stats created");
            message.put("data", createdBodyStats);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Error while creating body stats");
        }
        return message;
    }

    @PutMapping("/update/{bodyStatsId}")
//    @PreAuthorize("hasAuthority('update_bodyStats')")
    public Map<String, Object> updateBodyStats(@PathVariable Long bodyStatsId, @RequestBody BodyStatsDto bodyStatsDto) {
        Map<String, Object> message = new HashMap<>();
        BodyStatsDto updatedBodyStats = bodyStatsService.updateBodyStats(bodyStatsId, bodyStatsDto);
        if (updatedBodyStats != null) {
            message.put("status", 200);
            message.put("message", "body Stats updated");
            message.put("data", updatedBodyStats);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Error while updating body stats");
        }
        return message;
    }

    @GetMapping("/getAllBodyStats")
    @PreAuthorize("hasAuthority('view_all_bodyStats')")
    public Map<String, Object> getAllBodyStats() {
        Map<String, Object> message = new HashMap<>();

        List<BodyStatsDto> bodyStatsList = bodyStatsService.getAllBodyStats();

        if (!bodyStatsList.isEmpty()) {
            message.put("status", 200);
            message.put("message", "retrieved Body stats");
            message.put("data", bodyStatsList);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Error while retrieving body stats");
        }
        return message;
    }

    @GetMapping("/getBodyStatsById/{bodyStatsId}")
    @PreAuthorize("hasAuthority('view_bodyStats')")
    public Map<String, Object> getBodyStatsById(@PathVariable Long bodyStatsId) {
        Map<String, Object> message = new HashMap<>();
        BodyStatsDto bodyStatsDto = bodyStatsService.getBodyStatsById(bodyStatsId);
        if (bodyStatsDto != null) {
            message.put("status",  200);
            message.put("message","retrieved body stats");
        } else {
            message.put("status", 500);
            message.put("message", "Error while retrieving body stats");
        }
        return message;
    }

    @GetMapping("/getBodyStatsByUserId")
    @PreAuthorize("hasAuthority('view_bodyStats')")
    public Map<String, Object> getBodyStatsByUser(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        BodyStatsDto bodyStats = bodyStatsService.getBodyStatsByUser(principal);
        if (bodyStats != null) {
            message.put("status", 200);
            message.put("message", "retrieved body stats for user");
            message.put("data", bodyStats);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Error while retrieving body stats for user");
        }
        return message;
    }

    @DeleteMapping("/delete/{bodyStatsId}")
    @PreAuthorize("hasAuthority('delete_bodyStats')")
    public Map<String, Object> deleteBodyStats(@PathVariable Long bodyStatsId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        try {
            bodyStatsService.deleteBodyStats(bodyStatsId);
            message.put("status", 200);
            message.put("message", "BodyStats deleted successfully");
        } catch (Exception e) {
            message.clear();
            message.put("status", HttpStatus.NOT_FOUND);
            message.put("message", e.getMessage());
        }
        return message;
    }
}
