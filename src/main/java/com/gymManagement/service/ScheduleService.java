package com.gymManagement.service;

import com.gymManagement.dto.ScheduleDto;

import java.security.Principal;
import java.util.List;

public interface ScheduleService {
    ScheduleDto createSchedule(ScheduleDto scheduleDto);
    ScheduleDto updateSchedule(Long scheduleId, ScheduleDto scheduleDto);
    List<ScheduleDto> getAllSchedule();
    ScheduleDto getScheduleById(Long scheduleId);
    ScheduleDto addUserToSchedule(Long scheduleId, Principal principal);
    String deleteSchedule(Long scheduleId) throws Exception;
}
