package com.gymManagement.controller;

import com.gymManagement.dto.PlanHistoryDto;
import com.gymManagement.dto.SearchDto;
import com.gymManagement.model.User;
import com.gymManagement.repo.SubscriptionRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.PlanHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/planHistory")
public class PlanHistoryController {

    @Autowired
    private PlanHistoryService planHistoryService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_planHistory')")
    public Map<String, Object> createPlanHistory(@RequestBody PlanHistoryDto planHistoryDto) {
        Map<String, Object> message = new HashMap<>();
        PlanHistoryDto createdPlanHistory = planHistoryService.createPlanHistory(planHistoryDto);

        if(createdPlanHistory != null) {
            message.put("status", 200);
            message.put("message", "plan History created");
            message.put("data", createdPlanHistory);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while creating plan History");
        }
        return message;
    }

    @PutMapping("/update/{planHistoryId}")
    @PreAuthorize("hasAuthority('update_planHistory')")
    public Map<String, Object> updatePlanHistory(@PathVariable Long planHistoryId, @RequestBody PlanHistoryDto planHistoryDto) {
        Map<String, Object> message = new HashMap<>();

        PlanHistoryDto updatedPlanHistory = planHistoryService.updatePlanHistory(planHistoryId, planHistoryDto);

        if (updatedPlanHistory != null) {
            message.put("status", 200);
            message.put("message", "plan History updated");
            message.put("data", updatedPlanHistory);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while updating plan History");
        }

        return message;
    }

    @GetMapping("/getAllPlanHistory")
    @PreAuthorize("hasAuthority('view_all_planHistory')")
    public Map<String, Object> getAllPlanHistory(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        List<PlanHistoryDto> planHistoryList = planHistoryService.getAllPlanHistory();

        if (!planHistoryList.isEmpty()) {
            message.put("status", 200);
            message.put("message", "retrieved all plan history");
            message.put("data", planHistoryList);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving plan history");
        }
        message.put("fullName", fullName);
        return message;
    }

    @GetMapping("/getPlanHistoryById/{planHistoryId}")
    @PreAuthorize("hasAuthority('view_planHistory')")
    public Map<String, Object> getPlanHistoryById(@PathVariable Long planHistoryId) {
        Map<String, Object> message = new HashMap<>();
        PlanHistoryDto planHistory = planHistoryService.getPlanHistoryById(planHistoryId);
        if (planHistory != null) {
            message.put("status", 200);
            message.put("message", "plan History retrieved");
            message.put("data", planHistory);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving plan History");
        }

        return message;
    }

    @GetMapping("/getPlanHistoryByUserId")
    @PreAuthorize("hasAuthority('view_planHistory')")
    public Map<String, Object> getPlanHistoryByUserId(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " "  + loggedInUser.getLastName();
        List<PlanHistoryDto> planHistoryList = planHistoryService.getPlanHistoryByUserId(loggedInUser.getId());

        if(!planHistoryList.isEmpty()) {
            message.put("status", 200);
            message.put("message", "plan History retrieved");
            message.put("fullName", fullName);
            message.put("data", planHistoryList);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving plan History");
        }
        return message;
    }

    @DeleteMapping("/delete/{planHistoryId}")
    @PreAuthorize("hasAuthority('delete_planHistory')")
    public Map<String, Object> deletePlanHistory(@PathVariable Long planHistoryId) throws Exception {
        Map<String, Object> message = new HashMap<>();

        String retrievedMessage = planHistoryService.deletePlanHistory(planHistoryId);

        if (!retrievedMessage.isEmpty()) {
            message.put("status", 200);
            message.put("message", retrievedMessage);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while deleting plan History");
        }
        return message;
    }

    @GetMapping("/searchPlanHistoryUserBySubscription")
    public Map<String, Object> searchPlanHistoryUserBySubscription(@RequestBody SearchDto searchDto, Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " "  + loggedInUser.getLastName();

        Long subscriptionId = this.subscriptionRepo.findBySubscriptionName(searchDto.getSubscriptionName()).getSubscriptionId();

        List<PlanHistoryDto> planHistoryList = planHistoryService.searchBySubscriptionUser(loggedInUser.getId(), subscriptionId);

        if(!planHistoryList.isEmpty()) {
            message.put("status", 200);
            message.put("message", "plan History retrieved");
            message.put("fullName", fullName);
            message.put("data", planHistoryList);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving plan History");
        }
        return message;
    }

    @GetMapping("/searchPlanHistoryBySubscription")
    public Map<String, Object> searchPlanHistoryBySubscription(@RequestBody SearchDto searchDto, Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " "  + loggedInUser.getLastName();

        Long subscriptionId = this.subscriptionRepo.findBySubscriptionName(searchDto.getSubscriptionName()).getSubscriptionId();

        List<PlanHistoryDto> planHistoryList = planHistoryService.searchBySubscription(subscriptionId);

        if(!planHistoryList.isEmpty()) {
            message.put("status", 200);
            message.put("message", "plan History retrieved");
            message.put("fullName", fullName);
            message.put("data", planHistoryList);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving plan History");
        }
        return message;
    }
}
