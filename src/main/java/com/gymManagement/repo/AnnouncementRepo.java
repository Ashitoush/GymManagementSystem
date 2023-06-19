package com.gymManagement.repo;

import com.gymManagement.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByUserId(Long userId);

    @Query(value = "Select * from announcement Where announcement_date = ?1", nativeQuery = true)
    List<Announcement> findByAnnouncementDate(LocalDate date);
}
