package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.AchievementDTO;
import com.mundim.WeekMethod.entity.Achievement;
import com.mundim.WeekMethod.service.AchievementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/achievement")
@SecurityRequirement(name = "jwt")
public class AchievementContoller {

    private final AchievementService achievementService;

    public AchievementContoller(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody AchievementDTO dto){
        return new ResponseEntity<Achievement>(achievementService.createAchievement(dto), CREATED);
    }

    @GetMapping
    public ResponseEntity<Achievement> findAchievementById(@RequestParam Long id){
        return ResponseEntity.ok(achievementService.findAchievementById(id));
    }

    @PutMapping
    public ResponseEntity<Achievement> updateAchievementById(@RequestBody AchievementDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(achievementService.updateAchievementById(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<Achievement> deleteAchievementById(@RequestParam Long id){
        return ResponseEntity.ok(achievementService.deleteAchievementById(id));
    }
}
