package com.gymManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyStatsDto {
    private Long bodyStatsId;
    private Long bicep;
    private Long waist;
    private Double bmi;
    private String bmiStatus;
    private Double bodyFatPercentage;
    private Long height;
    private Long weight;
    private Long userId;
}
