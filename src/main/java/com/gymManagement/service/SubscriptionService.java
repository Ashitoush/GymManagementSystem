package com.gymManagement.service;

import com.gymManagement.dto.SubscriptionDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface SubscriptionService {
    SubscriptionDto createSubscription(SubscriptionDto subscriptionDto);
    SubscriptionDto updateSubscription(Long subscriptionId, SubscriptionDto subscriptionDto);
    List<SubscriptionDto> getAllSubscriptions();
    SubscriptionDto getSubscriptionById(Long subscriptionId);
    String deleteSubscription(Long subscriptionId) throws Exception;
    Integer getTotalSubscriptionCount();
    public Map<String, Object> getSubscriptionByUser(Principal principal);
}
