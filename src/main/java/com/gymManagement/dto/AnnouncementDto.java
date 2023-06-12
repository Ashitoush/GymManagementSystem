package com.gymManagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnnouncementDto {
    private Long announcementId;
    private String title;
    private String message;
    private LocalDate announcementDate;
}
