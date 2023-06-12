package com.gymManagement.repo;

import com.gymManagement.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionName(String name);
}
