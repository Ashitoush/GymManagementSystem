package com.gymManagement.repo;

import com.gymManagement.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByUserId(Long userId);
}
