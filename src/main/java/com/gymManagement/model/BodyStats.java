package com.gymManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "body_stats")
public class BodyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_stats_id")
    private Long bodyStatsId;
    @Column(name = "height")
    private Long height;
    @Column(name = "weight")
    private Long weight;
    @Column(name = "bicep")
    private Long bicep;
    @Column(name = "waist")
    private Long waist;
    @Column(name = "bmi")
    private Double bmi;
    @Column(name = "bmi_status")
    private String bmiStatus;
    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
