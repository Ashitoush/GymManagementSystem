package com.gymManagement.service.implementation;

import com.gymManagement.dto.AnnouncementDto;
import com.gymManagement.dto.SearchDto;
import com.gymManagement.helper.MergeSort;
import com.gymManagement.model.Announcement;
import com.gymManagement.model.User;
import com.gymManagement.repo.AnnouncementRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.AnnouncementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MergeSort mergeSort;
//    @Override
//    public AnnouncementDto createAnnouncement(AnnouncementDto announcementDto, Principal principal) {
//        User loggedInUser = userRepo.findByEmail(principal.getName());
//        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
//        announcement.setAnnouncementDate(LocalDate.now());
//        announcement.setUser(loggedInUser);
//
//        Announcement savedAnnouncement = announcementRepo.save(announcement);
//        return modelMapper.map(savedAnnouncement, AnnouncementDto.class);
//    }
    @Override
    public AnnouncementDto createAnnouncement(AnnouncementDto announcementDto, Principal principal) {
        User loggedInUser = userRepo.findByEmail(principal.getName());
        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        announcement.setAnnouncementDate(announcementDto.getAnnouncementDate());
        announcement.setUser(loggedInUser);

        Announcement savedAnnouncement = announcementRepo.save(announcement);
        return modelMapper.map(savedAnnouncement, AnnouncementDto.class);
    }

    @Override
    public AnnouncementDto updateAnnouncement(Long announcementId, AnnouncementDto announcementDto) throws Exception {
        Optional<Announcement> optionalAnnouncement = announcementRepo.findById(announcementId);
        if (optionalAnnouncement.isPresent()) {
            Announcement announcement = optionalAnnouncement.get();
            announcement.setTitle(announcementDto.getTitle());
            announcement.setMessage(announcementDto.getMessage());

            Announcement updatedAnnouncement = announcementRepo.save(announcement);
            return modelMapper.map(updatedAnnouncement, AnnouncementDto.class);
        } else {
            throw new Exception("Announcement not found with ID: " + announcementId);
        }
    }

    @Override
    public List<AnnouncementDto> getAllAnnouncement() {
        List<Announcement> announcements = announcementRepo.findAll();
        return announcements.stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementDto getAnnouncementById(Long announcementId) throws Exception {
        Optional<Announcement> optionalAnnouncement = announcementRepo.findById(announcementId);
        if (optionalAnnouncement.isPresent()) {
            Announcement announcement = optionalAnnouncement.get();
            return modelMapper.map(announcement, AnnouncementDto.class);
        } else {
            throw new Exception("Announcement not found with ID: " + announcementId);
        }
    }

    @Override
    public String deleteAnnouncement(Long announcementId) throws Exception {
        if (announcementRepo.existsById(announcementId)) {
            announcementRepo.deleteById(announcementId);
            return "Announcement deleted successfully.";
        } else {
            throw new Exception("Announcement not found with ID: " + announcementId);
        }
    }

    @Override
    public List<AnnouncementDto> getAnnouncementsByUser() {
        List<Announcement> announcements = announcementRepo.findAll();

        Comparator<Announcement> comparator = Comparator.comparing(Announcement::getAnnouncementDate).reversed();
        announcements = this.mergeSort.mergeSort(announcements, comparator);

        List<AnnouncementDto> announcementDtos = announcements.stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementDto.class))
                .limit(3) // Limit the stream to the first 3 elements
                .collect(Collectors.toList());

        return announcementDtos;
    }

    @Override
    public List<AnnouncementDto> searchAnnouncementByDate(SearchDto searchDto) {
        LocalDate date = searchDto.getDate();

        List<Announcement> announcements = announcementRepo.findByAnnouncementDate(date);

        List<AnnouncementDto> announcementDtos = toAnnouncementDto(announcements);

//        List<AnnouncementDto> announcementDtos = announcements.stream()
//                .map(announcement -> modelMapper.map(announcement, AnnouncementDto.class))
//                .collect(Collectors.toList());

        return announcementDtos;
    }

    private List<AnnouncementDto> toAnnouncementDto(List<Announcement> announcements) {
        List<AnnouncementDto> announcementDtos = new ArrayList<>();

        for (Announcement announcement : announcements) {
            AnnouncementDto announcementDto = new AnnouncementDto();

            announcementDto.setAnnouncementId(announcement.getAnnouncementId());
            announcementDto.setAnnouncementDate(announcement.getAnnouncementDate());
            announcementDto.setTitle(announcement.getTitle());
            announcementDto.setMessage(announcement.getMessage());

            announcementDtos.add(announcementDto);
        }

        return announcementDtos;
    }

}
