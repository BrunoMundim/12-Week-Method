package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.AchievementDTO;
import com.mundim.WeekMethod.entity.Achievement;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.AchievementRepository;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserService userService;

    public AchievementService(AchievementRepository achievementRepository, UserService userService) {
        this.achievementRepository = achievementRepository;
        this.userService = userService;
    }

    public Achievement createAchievement(AchievementDTO dto){
        verifyAchievementDto(dto);
        return achievementRepository.save(new Achievement(dto));
    }

    public Achievement findAchievementById(Long id){
        return achievementRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Achievement not found"));
    }

    public Achievement updateAchievementById(Long id, AchievementDTO dto){
        Achievement achievement = findAchievementById(id);
        if(dto.userId() != null) achievement.setUserId(dto.userId());
        if(dto.description() != null) achievement.setDescription(dto.description());
        if(dto.dateAchieved() != null) achievement.setDateAchieved(dto.dateAchieved());
        return achievementRepository.save(achievement);
    }

    public Achievement deleteAchievementById(Long id){
        Achievement achievement = findAchievementById(id);
        achievementRepository.deleteById(id);
        return achievement;
    }

    private void verifyAchievementDto(AchievementDTO dto){
        userService.findUserById(dto.userId());
        if(dto.description() == null) throw new BadRequestException("Description field cannot be null");
        if(dto.dateAchieved() == null) throw new BadRequestException("Date achieved field cannot be null");
    }

}
