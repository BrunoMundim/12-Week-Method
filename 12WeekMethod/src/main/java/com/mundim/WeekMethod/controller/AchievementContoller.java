package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.AchievementDTO;
import com.mundim.WeekMethod.entity.Achievement;
import com.mundim.WeekMethod.service.AchievementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/achievement")
@Api(tags = "achievement")
public class AchievementContoller {

    private final AchievementService achievementService;

    public AchievementContoller(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping
    @ApiOperation("Create an achievement")
    public ResponseEntity<Achievement> createAchievement(@RequestBody AchievementDTO dto){
        return new ResponseEntity<Achievement>(achievementService.createAchievement(dto), CREATED);
    }

    @GetMapping
    @ApiOperation("Find an achievement by id")
    public ResponseEntity<Achievement> findAchievementById(@RequestParam Long id){
        return ResponseEntity.ok(achievementService.findAchievementById(id));
    }

    @PutMapping
    @ApiOperation("Update an achievement by id")
    public ResponseEntity<Achievement> updateAchievementById(@RequestBody AchievementDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(achievementService.updateAchievementById(id, dto));
    }

    @GetMapping
    @ApiOperation("Delete an achievement by id")
    public ResponseEntity<Achievement> deleteAchievementById(@RequestParam Long id){
        return ResponseEntity.ok(achievementService.deleteAchievementById(id));
    }
}
