package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.service.TaskService;
import com.mundim.WeekMethod.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/task")
@SecurityRequirement(name = "jwt")
public class TaskController {

    private final TaskService taskService;
    private final MailService mailService;

    public TaskController(TaskService taskService, MailService mailService) {
        this.taskService = taskService;
        this.mailService = mailService;
    }

    @PostMapping
    @Operation(tags = "Task", summary = "Create a task")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<Task>(taskService.createTask(taskDTO), CREATED);
    }

    @GetMapping
    @Operation(tags = "Task", summary = "Find a task by ID")
    public ResponseEntity<Task> findTaskById(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @GetMapping("/weekcard-tasks")
    @Operation(tags = "Task", summary = "Find tasks by Week Card ID")
    public ResponseEntity<List<Task>> findTasksByWeekCardId(@RequestParam Long weekCardId) {
        return ResponseEntity.ok(taskService.findTasksByWeekCardId(weekCardId));
    }

    @PutMapping
    @Operation(tags = "Task", summary = "Update a task")
    public ResponseEntity<Task> updateTaskById(
            @RequestParam Long taskId,
            @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTaskById(taskId, taskDTO));
    }

    @PutMapping("/in-progress-task")
    @Operation(tags = "Task", summary = "Change task status to in progress")
    public ResponseEntity<Task> inProgressTask(
            @RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.inProgressTask(taskId));
    }

    @PutMapping("/complete-task")
    @Operation(tags = "Task", summary = "Change task status to completed")
    public ResponseEntity<Task> completeTask(
            @RequestParam Long taskId) {
        Task task = taskService.completeTask(taskId);
        mailService.sendEmailWithTemplate(("Completed Task: " + task.getTitle()), "CompletedTask.html", task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping
    @Operation(tags = "Task", summary = "Delete a task")
    public ResponseEntity<Task> deleteTaskById(
            @RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.deleteTaskById(taskId));
    }

}
