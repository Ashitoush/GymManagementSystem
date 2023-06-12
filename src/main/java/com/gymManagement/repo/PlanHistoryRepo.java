package com.gymManagement.repo;

import com.gymManagement.model.BodyStats;
import com.gymManagement.model.PlanHistory;
import com.gymManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanHistoryRepo extends JpaRepository<PlanHistory, Long> {
    List<PlanHistory> findByUserId(Long userId);
    List<PlanHistory> findByUserOrderByEndDateDesc(User user);
}
