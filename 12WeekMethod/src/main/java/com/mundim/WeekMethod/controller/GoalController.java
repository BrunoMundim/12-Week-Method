package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.dto.update.UpdateGoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.service.GoalService;
import com.mundim.WeekMethod.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@SecurityRequirement(name = "jwt")
public class GoalController {

    private final GoalService goalService;
    private final MailService mailService;

    @Autowired
    public GoalController(GoalService goalService, MailService mailService) {
        this.goalService = goalService;
        this.mailService = mailService;
    }

    @PostMapping
    @Operation(tags = "Goal", summary = "Create a goal for the logged user")
    public ResponseEntity<Goal> createGoal(@RequestBody GoalDTO goalDTO) {
        return new ResponseEntity<Goal>(goalService.createGoalForLoggedUser(goalDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(tags = "Goal", summary = "Find a goal by ID")
    public ResponseEntity<Goal> findGoalById(@RequestParam Long goalId) {
        return ResponseEntity.ok(goalService.findById(goalId));
    }

    @GetMapping("/find-all-by-id")
    @Operation(tags = "Goal", summary = "Find all goal from user id")
    public ResponseEntity<List<Goal>> findGoalsByUserId(Long userId) {
        return ResponseEntity.ok(goalService.findByUserId(userId));
    }

    @GetMapping("/find-all-logged-user")
    @Operation(tags = "Goal", summary = "Find all goal from logged user")
    public ResponseEntity<List<Goal>> findGoalsFromLoggedUser() {
        return ResponseEntity.ok(goalService.findFromLoggedUser());
    }

    @PutMapping
    @Operation(tags = "Goal", summary = "Update a goal by ID")
    public ResponseEntity<Goal> updateGoalById(@RequestParam Long goalId, @RequestBody UpdateGoalDTO dto) {
        return ResponseEntity.ok(goalService.updateGoal(goalId, dto));
    }

    @PutMapping("/in-progress")
    @Operation(tags = "Goal Status", summary = "Change a goal to IN PROGRESS")
    public ResponseEntity<Goal> inProgressGoal(@RequestParam Long goalId) {
        return ResponseEntity.ok(goalService.inProgressGoal(goalId));
    }

    @PutMapping("/complete")
    @Operation(tags = "Goal Status", summary = "Change a goal to COMPLETED")
    public ResponseEntity<Goal> completeGoal(@RequestParam Long goalId) {
        return ResponseEntity.ok(goalService.completeGoal(goalId));
    }

    @PutMapping("/key-result/complete")
    @Operation(tags = "Goal Key Result", summary = "Complete a key result")
    public ResponseEntity<Goal> completeKeyResult(@RequestParam Long goalId, Long keyResultId) {
        Goal goal = goalService.completeKeyResult(goalId, keyResultId);
        return ResponseEntity.ok(goal);
    }

    @PutMapping("/key-result/uncomplete")
    @Operation(tags = "Goal Key Result", summary = "Uncomplete a key result")
    public ResponseEntity<Goal> uncompleteKeyResult(@RequestParam Long goalId, Long keyResultId) {
        return ResponseEntity.ok(goalService.uncompleteKeyResult(goalId, keyResultId));
    }

    @DeleteMapping
    @Operation(tags = "Goal", summary = "Delete a goal by ID")
    public ResponseEntity<Goal> deleteGoalById(@RequestParam Long goalId) {
        return ResponseEntity.ok(goalService.deleteById(goalId));
    }

}
