package com.gymManagement.service;

import com.gymManagement.dto.AnnouncementDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    AnnouncementDto createAnnouncement(AnnouncementDto announcementDto, Principal principal);
    AnnouncementDto updateAnnouncement(Long scheduleId, AnnouncementDto announcementDto) throws Exception;
    List<AnnouncementDto> getAllAnnouncement();
    AnnouncementDto getAnnouncementById(Long announcementId) throws Exception;
    String deleteAnnouncement(Long announcementId) throws Exception;
    List<AnnouncementDto> getAnnouncementsByUser();
}
