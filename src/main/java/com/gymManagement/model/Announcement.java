package com.gymManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long announcementId;

    @Column(name = "title")
    private String title;
    @Column(name = "message")
    private String message;
    @Column(name = "announcement_date")
    private LocalDate announcementDate;
    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private User user;

}
