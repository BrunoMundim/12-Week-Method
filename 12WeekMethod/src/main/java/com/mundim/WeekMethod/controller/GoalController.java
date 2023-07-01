package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.service.GoalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
@Api(tags = "goals")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService){
        this.goalService = goalService;
    }

    @PostMapping
    @ApiOperation(value = "Create goal")
    public ResponseEntity<Goal> createGoal(@RequestBody GoalDTO goalDTO){
        return new ResponseEntity<Goal>(goalService.createGoal(goalDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Find goal by id")
    public ResponseEntity<Goal> findGoalById(@RequestParam Long goalId){
        return ResponseEntity.ok(goalService.findGoalById(goalId));
    }

    @PutMapping
    @ApiOperation(value = "Update goal by id")
    public ResponseEntity<Goal> updateGoalById(@RequestParam Long goalId, @RequestBody GoalDTO goalDTO){
        return ResponseEntity.ok(goalService.updateGoal(goalId, goalDTO));
    }

    @PutMapping("/key-result")
    @ApiOperation(value = "Add a key result")
    public ResponseEntity<Goal> addKeyResult(@RequestParam Long goalId, @RequestBody Goal.KeyResult keyResult){
        return ResponseEntity.ok(goalService.addKeyResult(goalId, keyResult));
    }

    @DeleteMapping
    @ApiOperation(value = "Delete goal by id")
    public ResponseEntity<Goal> deleteGoalById(@RequestParam Long goalId){
        return ResponseEntity.ok(goalService.deleteGoalById(goalId));
    }

}
