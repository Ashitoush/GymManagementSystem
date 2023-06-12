package com.gymManagement.controller;

import com.gymManagement.dto.SubscriptionDto;
import com.gymManagement.model.User;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_subscription')")
    public Map<String, Object> createSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        Map<String, Object> message = new HashMap<>();
        SubscriptionDto createdSubscription = subscriptionService.createSubscription(subscriptionDto);
        if(createdSubscription != null) {
            message.put("status", 200);
            message.put("message", "Subscription Created");
            message.put("data", createdSubscription);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot create subcription");
        }
        return message;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('update_subscription')")
    public Map<String, Object> updateSubscription(
            @PathVariable("id") Long subscriptionId,
            @RequestBody SubscriptionDto subscriptionDto
    ) {
        Map<String, Object> message = new HashMap<>();
        SubscriptionDto updatedSubscription = subscriptionService.updateSubscription(subscriptionId, subscriptionDto);
        if (updatedSubscription != null) {
            message.put("status", 200);
            message.put("message", "subscription updated");
            message.put("data", updatedSubscription);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "Cannot create Subscription");
        }
        return message;
    }

    @GetMapping("/getAllSubscription")
    @PreAuthorize("hasAuthority('view_subscription')")
    public Map<String, Object> getAllSubscriptions(Principal principal) {
        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " +  loggedInUser.getLastName();
        Map<String, Object> message = new HashMap<>();
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        if (subscriptions != null) {
            message.put("status", 200);
            message.put("message", "Subscription Retrieved");
            message.put("fullName", fullName);
            message.put("data", subscriptions);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving subscription");
        }
        return message;
    }

    @GetMapping("/getSubscriptionById/{id}")
    @PreAuthorize("hasAuthority('view_subscription')")
    public Map<String, Object> getSubscriptionById(@PathVariable("id") Long subscriptionId) {
        Map<String, Object> message = new HashMap<>();
        SubscriptionDto subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription != null) {
            message.put("status", 200);
            message.put("message", "Subscription retrieved");
            message.put("data", subscription);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving subcription");
        }
        return message;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('delete_subscription')")
    public Map<String, Object> deleteSubscription(@PathVariable("id") Long subscriptionId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        String retrievedMessage = subscriptionService.deleteSubscription(subscriptionId);
        if (!message.isEmpty()) {
            message.put("status", 200);
            message.put("message", retrievedMessage);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while deleting subscription");
        }
        return message;
    }

    @GetMapping("/subscriptionCount")
    @PreAuthorize("hasAuthority('view_subscription_count')")
    public Map<String, Object> getTotalSubscriptionCount() {
        Map<String, Object> message = new HashMap<>();
        int count = subscriptionService.getTotalSubscriptionCount();
        if (count >= 0) {
            message.put("status", 200);
            message.put("message", "Subscription count retrieved");
            message.put("data", count);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving subscription count");
        }
        return message;
    }
}