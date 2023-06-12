package com.gymManagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PlanHistoryDto {
    private Long planHistoryId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long subscriptionId;
    private String subscriptionName;
    private String nameOfUser;
    private Long userId;
}
