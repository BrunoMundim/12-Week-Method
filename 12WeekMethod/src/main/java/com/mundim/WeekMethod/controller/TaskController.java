package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/task")
@Api(tags = "task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ApiOperation(value = "Create Task")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<Task>(taskService.createTask(taskDTO), CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Find Task by ID")
    public ResponseEntity<Task> findTaskById(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @PutMapping
    @ApiOperation(value = "Update Task by ID")
    public ResponseEntity<Task> updateTaskById(
            @RequestParam Long taskId,
            @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTaskById(taskId, taskDTO));
    }

    @DeleteMapping
    @ApiOperation(value = "Delete Task by ID")
    public ResponseEntity<Task> deleteTaskById(
            @RequestParam Long taskId,
            @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.deleteTaskById(taskId));
    }

}
