package com.gymManagement.service;

import com.gymManagement.dto.PlanHistoryDto;
import com.gymManagement.model.PlanHistory;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface PlanHistoryService {
    PlanHistoryDto createPlanHistory(PlanHistoryDto planHistoryDto);
    PlanHistoryDto updatePlanHistory(Long planHistoryId, PlanHistoryDto planHistoryDto);
    List<PlanHistoryDto> getAllPlanHistory();
    PlanHistoryDto getPlanHistoryById(Long planHistoryId);
    List<PlanHistoryDto> getPlanHistoryByUserId(Long userId);
    String deletePlanHistory(Long planHistoryId) throws Exception;
    Map<String, Object> calculateRemainingDays(Principal principal);
    List<PlanHistoryDto> searchBySubscription(Long subscriptionId);
    List<PlanHistoryDto> searchBySubscriptionUser(Long userId, Long subscriptionId);
}
