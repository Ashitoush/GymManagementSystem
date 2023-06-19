package com.gymManagement.service.implementation;

import com.gymManagement.dto.PlanHistoryDto;
import com.gymManagement.model.PlanHistory;
import com.gymManagement.model.Subscription;
import com.gymManagement.model.User;
import com.gymManagement.repo.PlanHistoryRepo;
import com.gymManagement.repo.SubscriptionRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.PlanHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanHistoryServiceImpl implements PlanHistoryService {

    @Autowired
    private PlanHistoryRepo planHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PlanHistoryDto createPlanHistory(PlanHistoryDto planHistoryDto) {
        PlanHistory planHistory = modelMapper.map(planHistoryDto, PlanHistory.class);
//        User user = getUserById(planHistoryDto.getUserId());
//        Subscription subscription = getSubscriptionById(planHistoryDto.getSubscriptionId());
        User user = this.userRepo.findByUserName(planHistoryDto.getNameOfUser());
        Subscription subscription = this.subscriptionRepo.findBySubscriptionName(planHistoryDto.getSubscriptionName());
        planHistory.setUser(user);
        planHistory.setSubscription(subscription);
        PlanHistory savedPlanHistory = planHistoryRepo.save(planHistory);
        return modelMapper.map(savedPlanHistory, PlanHistoryDto.class);
    }

    @Override
    public PlanHistoryDto updatePlanHistory(Long planHistoryId, PlanHistoryDto planHistoryDto) {
        Optional<PlanHistory> optionalPlanHistory = planHistoryRepo.findById(planHistoryId);
        if (optionalPlanHistory.isPresent()) {
            PlanHistory planHistory = optionalPlanHistory.get();
            planHistory.setStartDate(planHistoryDto.getStartDate());
            planHistory.setEndDate(planHistoryDto.getEndDate());
            User user = getUserById(planHistoryDto.getUserId());
            Subscription subscription = getSubscriptionById(planHistoryDto.getSubscriptionId());
            planHistory.setUser(user);
            planHistory.setSubscription(subscription);
            PlanHistory updatedPlanHistory = planHistoryRepo.save(planHistory);
            return modelMapper.map(updatedPlanHistory, PlanHistoryDto.class);
        }
        return null;
    }

    @Override
    public List<PlanHistoryDto> getAllPlanHistory() {
        List<PlanHistory> planHistoryList = planHistoryRepo.findAll();
        return planHistoryList.stream()
                .map(planHistory -> modelMapper.map(planHistory, PlanHistoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PlanHistoryDto getPlanHistoryById(Long planHistoryId) {
        Optional<PlanHistory> optionalPlanHistory = planHistoryRepo.findById(planHistoryId);
        return optionalPlanHistory.map(planHistory -> modelMapper.map(planHistory, PlanHistoryDto.class)).orElse(null);
    }

    @Override
    public List<PlanHistoryDto> getPlanHistoryByUserId(Long userId) {
        List<PlanHistory> planHistoryList = planHistoryRepo.findByUserId(userId);
        List<PlanHistoryDto> planHistoryDtoList = new ArrayList<>();

        for (PlanHistory planHistory : planHistoryList) {
            PlanHistoryDto planHistoryDto = modelMapper.map(planHistory, PlanHistoryDto.class);
            String subscriptionName = planHistory.getSubscription().getSubscriptionName();
            planHistoryDto.setSubscriptionName(subscriptionName);
            planHistoryDtoList.add(planHistoryDto);
        }

        return planHistoryDtoList;
    }


    @Override
    public String deletePlanHistory(Long planHistoryId) throws Exception {
        if (planHistoryRepo.existsById(planHistoryId)) {
            planHistoryRepo.deleteById(planHistoryId);
            return "PlanHistory deleted successfully";
        } else {
            throw new Exception("PlanHistory not found with Id: " + planHistoryId);
        }
    }

    @Override
    public Map<String, Object> calculateRemainingDays(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());

        PlanHistory latestPlanHistory = getLatestPlanHistory(loggedInUser);

        message.put("status", 200);
        message.put("data", latestPlanHistory);

        return message;
    }

    @Override
    public List<PlanHistoryDto> searchBySubscriptionUser(Long userId, Long subscriptionId) {
        List<PlanHistory> planHistoryList = this.planHistoryRepo.findBySubscription(subscriptionId, userId);

        List<PlanHistoryDto> planHistoryDtoList = new ArrayList<>();

        for (PlanHistory planHistory : planHistoryList) {
            PlanHistoryDto planHistoryDto = modelMapper.map(planHistory, PlanHistoryDto.class);
            String subscriptionName = planHistory.getSubscription().getSubscriptionName();
            planHistoryDto.setSubscriptionName(subscriptionName);
            planHistoryDtoList.add(planHistoryDto);
        }
        return planHistoryDtoList;
    }

    @Override
    public List<PlanHistoryDto> searchBySubscription(Long subscriptionId) {
        List<PlanHistory> planHistoryList = this.planHistoryRepo.findBySubscription(subscriptionId);

        List<PlanHistoryDto> planHistoryDtoList = new ArrayList<>();

        for (PlanHistory planHistory : planHistoryList) {
            PlanHistoryDto planHistoryDto = modelMapper.map(planHistory, PlanHistoryDto.class);
            String subscriptionName = planHistory.getSubscription().getSubscriptionName();
            planHistoryDto.setSubscriptionName(subscriptionName);
            planHistoryDtoList.add(planHistoryDto);
        }
        return planHistoryDtoList;
    }


    private User getUserById(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        return optionalUser.orElse(null);
    }

    private Subscription getSubscriptionById(Long subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(subscriptionId);
        return optionalSubscription.orElse(null);
    }

    private PlanHistory getLatestPlanHistory(User user) {
        List<PlanHistory> planHistoryList = planHistoryRepo.findByUserOrderByEndDateDesc(user);

        Optional<PlanHistory> latestPlanHistory = planHistoryList.stream()
                .findFirst();

        return latestPlanHistory.orElse(null);
    }
}
