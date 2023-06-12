package com.gymManagement.controller;

import com.gymManagement.dto.AnnouncementDto;
import com.gymManagement.helper.MergeSort;
import com.gymManagement.model.Announcement;
import com.gymManagement.model.Payment;
import com.gymManagement.model.User;
import com.gymManagement.repo.AnnouncementRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.AnnouncementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MergeSort mergeSort;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_announcement')")
    public Map<String, Object> createAnnouncement(@RequestBody AnnouncementDto announcementDto, Principal principal) {
        Map<String, Object> message = new HashMap<>();

        AnnouncementDto createdAnnouncement = announcementService.createAnnouncement(announcementDto, principal);

        if (createdAnnouncement != null) {
            message.put("status", 200);
            message.put("message", "Announcement created");
            message.put("data", createdAnnouncement);
        } else {
            message.put("status", 500);
            message.put("message", "Error announcement");
        }
        return message;
    }

    @PutMapping("/update/{announcementId}")
    @PreAuthorize("hasAuthority('update_announcement')")
    public Map<String, Object> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody AnnouncementDto announcementDto
    ) throws Exception {
        Map<String, Object> message = new HashMap<>();
        AnnouncementDto updatedAnnouncement = announcementService.updateAnnouncement(announcementId, announcementDto);
        if (updatedAnnouncement != null) {
            message.put("status", 200);
            message.put("message", "Announcement updated");
            message.put("data", updatedAnnouncement);
        } else {
            message.put("status", 500);
            message.put("message", "Error while creating Announcement");
        }
        return message;
    }

    @GetMapping("/getAllAnnouncement")
    @PreAuthorize("hasAuthority('view_announcement')")
    public Map<String, Object> getAllAnnouncements(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
        List<Announcement> announcements  = this.announcementRepo.findAll();
        Comparator<Announcement> comparator = Comparator.comparing(Announcement::getAnnouncementDate).reversed();
        announcements = this.mergeSort.mergeSort(announcements, comparator);

        List<AnnouncementDto> announcementsDto = announcements.stream().map(announcement -> this.modelMapper.map(announcement, AnnouncementDto.class)).collect(Collectors.toList());

        if (!announcementsDto.isEmpty()) {
            message.put("status", 200);
            message.put("message", "Retrieved announcements successfully");
            message.put("fullName", fullName);
            message.put("data", announcementsDto);
        } else {
            message.put("status", 500);
            message.put("message", "Error while retrieving announcement");
        }
        return message;
    }

    @GetMapping("/getAnnouncementById/{announcementId}")
    @PreAuthorize("hasAuthority('view_announcement')")
    public Map<String, Object> getAnnouncementById(@PathVariable Long announcementId) throws Exception {
        Map<String, Object> message = new HashMap<>();

        AnnouncementDto announcement = announcementService.getAnnouncementById(announcementId);

        if (announcement != null) {
            message.put("status", 200);
            message.put("message", "Announcement retrieved successfully");
            message.put("data", announcement);
        } else {
            message.put("status", 500);
            message.put("message", "Error while retrieving Announcement");
        }
        return message;
    }

    @DeleteMapping("/delete/{announcementId}")
    @PreAuthorize("hasAuthority('delete_announcement')")
    public Map<String, Object> deleteAnnouncement(@PathVariable Long announcementId) throws Exception {
        Map<String, Object> message = new HashMap<>();

        String retrievedMessage = announcementService.deleteAnnouncement(announcementId);

        if(!retrievedMessage.isEmpty()) {
            message.put("status", 200);
            message.put("message", retrievedMessage);
        } else {
            message.put("status", 500);
            message.put("message", "Error while deleting announcement");
        }
        return message;
    }
}
