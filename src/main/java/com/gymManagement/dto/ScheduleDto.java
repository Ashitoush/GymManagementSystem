package com.gymManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDto {
    private Long scheduleId;
    private Long capacity;
    private LocalTime startTime;
    private LocalTime endTime;
    private String instructorName;
    private List<Long> userIdList;
}
