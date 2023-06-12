package com.gymManagement.service;

import com.gymManagement.dto.BodyStatsDto;

import java.security.Principal;
import java.util.List;

public interface BodyStatsService {

    BodyStatsDto createBodyStats(BodyStatsDto bodyStatsDto);

    BodyStatsDto updateBodyStats(Long bodyStatsId, BodyStatsDto bodyStatsDto);

    List<BodyStatsDto> getAllBodyStats();
    BodyStatsDto getBodyStatsById(Long userId);

    BodyStatsDto getBodyStatsByUser(Principal principal);

    String deleteBodyStats(Long bodyStatsId) throws Exception;
}
