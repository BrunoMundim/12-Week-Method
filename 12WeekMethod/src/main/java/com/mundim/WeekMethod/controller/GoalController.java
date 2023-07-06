package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.service.GoalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO Criar goals view para mostrar datas corretamente

@RestController
@RequestMapping("/goals")
@SecurityRequirement(name = "jwt")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService){
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalDTO goalDTO){
        return new ResponseEntity<Goal>(goalService.createGoal(goalDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Goal> findGoalById(@RequestParam Long goalId){
        return ResponseEntity.ok(goalService.findGoalById(goalId));
    }

    @PutMapping
    public ResponseEntity<Goal> updateGoalById(@RequestParam Long goalId, @RequestBody GoalDTO goalDTO){
        return ResponseEntity.ok(goalService.updateGoal(goalId, goalDTO));
    }

    @PutMapping("/key-result")
    public ResponseEntity<Goal> addKeyResult(@RequestParam Long goalId, @RequestBody Goal.KeyResult keyResult){
        return ResponseEntity.ok(goalService.addKeyResult(goalId, keyResult));
    }

    @DeleteMapping
    public ResponseEntity<Goal> deleteGoalById(@RequestParam Long goalId){
        return ResponseEntity.ok(goalService.deleteGoalById(goalId));
    }

}
