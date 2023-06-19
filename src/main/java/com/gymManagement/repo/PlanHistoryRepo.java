package com.gymManagement.repo;

import com.gymManagement.model.BodyStats;
import com.gymManagement.model.PlanHistory;
import com.gymManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanHistoryRepo extends JpaRepository<PlanHistory, Long> {
    List<PlanHistory> findByUserId(Long userId);
    List<PlanHistory> findByUserOrderByEndDateDesc(User user);
    @Query(value = "Select * from plan_history Where subscription_id_fk = ?1 And user_id_fk = ?2", nativeQuery = true)
    List<PlanHistory> findBySubscription(Long subscriptionId, Long userId);
    @Query(value = "Select * from plan_history Where subscription_id_fk = ?1", nativeQuery = true)
    List<PlanHistory> findBySubscription(Long subscriptionId);
}
