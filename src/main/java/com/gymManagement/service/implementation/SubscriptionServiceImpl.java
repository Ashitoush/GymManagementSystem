package com.gymManagement.service.implementation;

import com.gymManagement.dto.PlanHistoryDto;
import com.gymManagement.dto.SubscriptionDto;
import com.gymManagement.model.PlanHistory;
import com.gymManagement.model.Subscription;
import com.gymManagement.model.User;
import com.gymManagement.repo.SubscriptionRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.PlanHistoryService;
import com.gymManagement.service.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PlanHistoryService planHistoryService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubscriptionDto createSubscription(SubscriptionDto subscriptionDto) {
        Subscription subscription = modelMapper.map(subscriptionDto, Subscription.class);
        Subscription savedSubscription = subscriptionRepo.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDto.class);
    }

    @Override
    public SubscriptionDto updateSubscription(Long subscriptionId, SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription != null) {
            subscription.setSubscriptionName(subscriptionDto.getSubscriptionName());
            subscription.setSubscriptionDescription(subscriptionDto.getSubscriptionDescription());
            subscription.setDuration(subscriptionDto.getDuration());
            subscription.setAmount(subscriptionDto.getAmount());

            Subscription updatedSubscription = subscriptionRepo.save(subscription);
            return modelMapper.map(updatedSubscription, SubscriptionDto.class);
        }
        return null;
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepo.findAll();
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionDto getSubscriptionById(Long subscriptionId) {
        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription != null) {
            return modelMapper.map(subscription, SubscriptionDto.class);
        }
        return null;
    }

    @Override
    public String deleteSubscription(Long subscriptionId) throws Exception {
        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription != null) {
            subscriptionRepo.deleteById(subscriptionId);
            return "Subscription deleted successfully.";
        } else {
            throw new Exception("Subscription not found with id: " + subscriptionId);
        }
    }

    @Override
    public Integer getTotalSubscriptionCount() {
        return subscriptionRepo.findAll().size();
    }

    @Override
    public Map<String, Object> getSubscriptionByUser(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = userRepo.findByEmail(principal.getName());
        List<PlanHistoryDto> planHistoryDtoList = planHistoryService.getPlanHistoryByUserId(loggedInUser.getId());

        if (!planHistoryDtoList.isEmpty()) {
            // Sort the plan history list in descending order based on the start date
            planHistoryDtoList.sort(Comparator.comparing(PlanHistoryDto::getStartDate).reversed());

            PlanHistoryDto latestPlanHistory = planHistoryDtoList.get(0); // Get the latest plan history
            Subscription subscription = subscriptionRepo.findById(latestPlanHistory.getSubscriptionId()).get();

            if (subscription != null) {
                message.put("status", 200);
                message.put("subscriptionId", subscription.getSubscriptionId());
                message.put("subscriptionName", subscription.getSubscriptionName());
                message.put("subscriptionDuration", subscription.getDuration());
            } else {
                message.put("status", 500);
                message.put("message", "Unable to retrieve subscription");
            }
        } else {
            message.put("status", 500);
            message.put("message", "No plan history found for the user");
        }
        return message;
    }
}
