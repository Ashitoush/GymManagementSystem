package com.gymManagement.controller;

import com.gymManagement.dto.AnnouncementDto;
import com.gymManagement.dto.BodyStatsDto;
import com.gymManagement.model.Announcement;
import com.gymManagement.model.PlanHistory;
import com.gymManagement.model.User;
import com.gymManagement.repo.PlanHistoryRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class UserDashboardController {
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BodyStatsService bodyStatsService;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private PlanHistoryService planHistoryService;
    @Autowired
    private PlanHistoryRepo planHistoryRepo;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('userDashboard')")
    public Map<String, Object> userDashboard(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        message.put("fullName", fullName);

        Map<String, Object> remainingDays = this.planHistoryService.calculateRemainingDays(principal);
        Object planHistoryData = remainingDays.get("data");

        PlanHistory planHistory = (PlanHistory) planHistoryData;
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = planHistory.getEndDate();
        Long remainingDaysValue = ChronoUnit.DAYS.between(currentDate, endDate);
        message.put("status", 200);
        message.put("remainingDays", Math.max(0, remainingDaysValue));
        message.put("expirationDate", endDate);

        Map<String, Object> subscription = this.subscriptionService.getSubscriptionByUser(principal);
        message.put("subscriptionId", subscription.get("subscriptionId"));
        message.put("subscriptionName", subscription.get("subscriptionName"));
        message.put("subscriptionDuration", subscription.get("subscriptionDuration"));

        Map<String, Object> payment = this.paymentService.calculateDueAmount(principal);
        message.put("dueAmount", payment.get("totalDueAmount"));

        payment = this.paymentService.getLastPaymentAmount(principal);

        message.put("lastPaymentAmount", payment.get("lastPaymentAmount"));
        message.put("paymentDate", payment.get("lastPaymentDate"));

        List<AnnouncementDto> announcement = this.announcementService.getAnnouncementsByUser();
        message.put("announcementData", announcement);

        BodyStatsDto bodyStats = this.bodyStatsService.getBodyStatsByUser(principal);
        message.put("bodyStats", bodyStats);

        return message;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('adminDashboard')")
    public Map<String, Object> adminDashboard(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        message.put("fullName", fullName);

        Long totalUser = this.userService.getTotalUserCount();
        message.put("totalUser", totalUser);

        Long totalSubcription = this.subscriptionService.getTotalSubscriptionCount().longValue();
        message.put("totalSubscription", totalSubcription);

        Long amountCollected = this.paymentService.getTotalAmountCollectedThisMonth().longValue();
        message.put("totalAmountCollected", amountCollected);

        Long dueAmount = this.paymentService.getTotalDueAmount().longValue();
        message.put("dueAmount", dueAmount);

        return message;
    }

}
