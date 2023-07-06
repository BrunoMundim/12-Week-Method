package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.UnauthorizedRequestException;
import com.mundim.WeekMethod.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeekCardService weekCardService;

    public TaskService(TaskRepository taskRepository, WeekCardService weekCardService) {
        this.taskRepository = taskRepository;
        this.weekCardService = weekCardService;
    }

    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task(taskDTO);
        task = taskRepository.save(task);

        try {
            weekCardService.addTaskToWeekCard(task.getId(), taskDTO.weekCardId());
        } catch (Exception e) {
            taskRepository.delete(task);
            throw new UnauthorizedRequestException(e.getMessage());
        }

        return task;
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException("Task not found"));
    }

    public Task updateTaskById(Long taskId, TaskDTO taskDTO) {
        Task task = findTaskById(taskId);
        if (taskDTO.weekCardId() != null) {
            weekCardService.addTaskToWeekCard(taskId, taskDTO.weekCardId());
            weekCardService.removeTaskFromWeekCard(taskId, task.getWeekCardId());
            task.setWeekCardId(taskDTO.weekCardId());
        }
        if (taskDTO.title() != null) task.setTitle(taskDTO.title());
        if (taskDTO.description() != null) task.setDescription(taskDTO.description());
        if (taskDTO.dueDate() != null) task.setDueDate(taskDTO.dueDate());
        return taskRepository.save(task);
    }

    public Task deleteTaskById(Long taskId) {
        Task task = findTaskById(taskId);
        weekCardService.removeTaskFromWeekCard(taskId, task.getWeekCardId());
        taskRepository.deleteById(taskId);
        return task;
    }

}
