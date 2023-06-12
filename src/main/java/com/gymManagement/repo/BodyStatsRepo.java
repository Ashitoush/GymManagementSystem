package com.gymManagement.repo;

import com.gymManagement.model.BodyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyStatsRepo extends JpaRepository<BodyStats, Long> {
    List<BodyStats> findListByUserId(Long userId);
    BodyStats findByUserId(Long userId);
}
