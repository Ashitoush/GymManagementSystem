package com.gymManagement.service.implementation;

import com.gymManagement.model.User;
import com.gymManagement.security.UserPrincipal;
import com.gymManagement.dto.BodyStatsDto;
import com.gymManagement.model.BodyStats;
import com.gymManagement.repo.BodyStatsRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.BodyStatsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BodyStatsServiceImpl implements BodyStatsService {

    @Autowired
    private BodyStatsRepo bodyStatsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BodyStatsDto createBodyStats(BodyStatsDto bodyStatsDto) {
        BodyStats bodyStats = modelMapper.map(bodyStatsDto, BodyStats.class);
        bodyStats = calculateAndAssignBMI(bodyStats); // Calculate and assign BMI and BMI status
        BodyStats savedBodyStats = bodyStatsRepo.save(bodyStats);
        return modelMapper.map(savedBodyStats, BodyStatsDto.class);
    }

    @Override
    public BodyStatsDto updateBodyStats(Long bodyStatsId, BodyStatsDto bodyStatsDto) {
        Optional<BodyStats> optionalBodyStats = bodyStatsRepo.findById(bodyStatsId);
        if (optionalBodyStats.isPresent()) {
            BodyStats bodyStats = optionalBodyStats.get();
            bodyStats.setBicep(bodyStatsDto.getBicep());
            bodyStats.setWaist(bodyStatsDto.getWaist());
            bodyStats.setBmi(bodyStatsDto.getBmi());
            bodyStats.setBmiStatus(bodyStatsDto.getBmiStatus());
            bodyStats.setBodyFatPercentage(bodyStatsDto.getBodyFatPercentage());
            bodyStats.setHeight(bodyStatsDto.getHeight());
            bodyStats.setWeight(bodyStatsDto.getWeight());
            bodyStats = calculateAndAssignBMI(bodyStats);
            BodyStats updatedBodyStats = bodyStatsRepo.save(bodyStats);
            return modelMapper.map(updatedBodyStats, BodyStatsDto.class);
        }
        return null;
    }

    @Override
    public List<BodyStatsDto> getAllBodyStats() {
        List<BodyStats> bodyStatsList = bodyStatsRepo.findAll();
        return bodyStatsList.stream()
                .map(bodyStats ->  modelMapper.map(bodyStats, BodyStatsDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public BodyStatsDto getBodyStatsById(Long bodyStatsId) {
        BodyStats bodyStats = bodyStatsRepo.findById(bodyStatsId).get();
        return this.modelMapper.map(bodyStats, BodyStatsDto.class);
    }

    @Override
    public BodyStatsDto getBodyStatsByUser(Principal principal) {
        User loggedInUser=userRepo.findByEmail(principal.getName());
        BodyStats bodyStats = bodyStatsRepo.findByUserId(loggedInUser.getId());

        if (bodyStats != null) {
            return modelMapper.map(bodyStats, BodyStatsDto.class);
        }
        return null;
    }

    @Override
    public String deleteBodyStats(Long bodyStatsId) throws Exception {
        if (bodyStatsRepo.existsById(bodyStatsId)) {
            bodyStatsRepo.deleteById(bodyStatsId);
            return "BodyStats deleted successfully";
        } else {
            throw new Exception("BodyStats not found with Id: " + bodyStatsId);
        }
    }

    private BodyStats calculateAndAssignBMI(BodyStats bodyStats) {
        double heightInMeters = bodyStats.getHeight() / 100.0; // Convert height to meters
        double bmi = bodyStats.getWeight() / (heightInMeters * heightInMeters);
        bodyStats.setBmi(bmi);

        if (bmi < 18.5) {
            bodyStats.setBmiStatus("Underweight");
        } else if (bmi >= 18.5 && bmi < 25) {
            bodyStats.setBmiStatus("Normal weight");
        } else if (bmi >= 25 && bmi < 30) {
            bodyStats.setBmiStatus("Overweight");
        } else {
            bodyStats.setBmiStatus("Obese");
        }
        return bodyStats;
    }
}
