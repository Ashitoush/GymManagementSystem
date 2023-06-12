package com.gymManagement.service.implementation;

import com.gymManagement.dto.ScheduleDto;
import com.gymManagement.model.Schedule;
import com.gymManagement.model.User;
import com.gymManagement.repo.ScheduleRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ScheduleDto createSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = this.modelMapper.map(scheduleDto, Schedule.class);
        Schedule savedSchedule = this.scheduleRepo.save(schedule);

        return this.modelMapper.map(savedSchedule, ScheduleDto.class);
    }

    @Override
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleDto scheduleDto) {
        Schedule schedule = this.scheduleRepo.findById(scheduleId).get();
        schedule.setCapacity(scheduleDto.getCapacity());
        schedule.setStartTime(scheduleDto.getStartTime());
        schedule.setEndTime(scheduleDto.getEndTime());

        Schedule updatedSchedule = this.scheduleRepo.save(schedule);
        return modelMapper.map(updatedSchedule, ScheduleDto.class);
    }


    @Override
    public List<ScheduleDto> getAllSchedule() {
        List<Schedule> schedules = this.scheduleRepo.findAll();
        return schedules.stream()
                .map(schedule -> this.modelMapper.map(schedule, ScheduleDto.class)).collect(Collectors.toList());
    }

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        Schedule schedule = this.scheduleRepo.findById(scheduleId).get();
        return this.modelMapper.map(schedule, ScheduleDto.class);
    }

    @Override
    public ScheduleDto addUserToSchedule(Long scheduleId, Principal principal) {
        Schedule schedule = this.scheduleRepo.findById(scheduleId).get();
        User loggedInUser = userRepo.findByEmail(principal.getName());

        schedule.getUsers().add(loggedInUser);
        Schedule updatedSchedule = this.scheduleRepo.save(schedule);
        return this.mapScheduleToDtoWithUserId(updatedSchedule);
    }

    @Override
    public String deleteSchedule(Long scheduleId) throws Exception {
        if (this.scheduleRepo.existsById(scheduleId)) {
            Schedule schedule = this.scheduleRepo.findById(scheduleId).get();

            List<User> users = schedule.getUsers();
            for (User user : users) {
                user.getSchedules().remove(schedule);
                this.userRepo.save(user);
            }

            this.scheduleRepo.deleteById(scheduleId);
            return "Schedule Deleted Successfully";
        } else {
            throw new Exception("Schedule not found with Id: " + scheduleId);
        }
    }

    public List<User> getUserFromId(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            Optional<User> userOptional = this.userRepo.findById(userId);
            userOptional.ifPresent(users::add);
        }
        return users;
    }

    public ScheduleDto mapScheduleToDtoWithUserId(Schedule schedule) {
        ScheduleDto scheduleDto = this.modelMapper.map(schedule, ScheduleDto.class);
        List<Long> userIds = schedule.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        scheduleDto.setUserIdList(userIds);

        return scheduleDto;
    }
}
