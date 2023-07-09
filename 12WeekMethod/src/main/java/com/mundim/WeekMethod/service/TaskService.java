package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.TaskRepository;
import com.mundim.WeekMethod.repository.WeekCardRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.TASK_NOT_FOUND_BY_ID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeekCardService weekCardService;
    private final WeekCardRepository weekCardRepository;

    public TaskService(
            TaskRepository taskRepository,
            WeekCardService weekCardService,
            WeekCardRepository weekCardRepository
    ) {
        this.taskRepository = taskRepository;
        this.weekCardService = weekCardService;
        this.weekCardRepository = weekCardRepository;
    }

    public Task createTask(TaskDTO taskDTO) {
        weekCardService.verifyUserAuthorizationForWeekCard(taskDTO.weekCardId());
        Task task = new Task(taskDTO);
        task = taskRepository.save(task);
        addTaskToWeekCard(taskDTO.weekCardId(), task.getId());
        return task;
    }

    public Task findTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND_BY_ID.params(taskId.toString()).getMessage()));
        verifyUserAuthorizationForTask(task);
        return task;
    }

    public List<Task> findTasksByWeekCardId (Long weekCardId) {
        weekCardService.verifyUserAuthorizationForWeekCard(weekCardId);
        return taskRepository.findTasksByWeekCardId(weekCardId);
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

    public Task inProgressTask(Long taskId) {
        Task task = findTaskById(taskId);
        task.setStatus(Task.TaskStatus.IN_PROGRESS);
        return taskRepository.save(task);
    }

    public Task completeTask(Long taskId) {
        Task task = findTaskById(taskId);
        task.setStatus(Task.TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }

    public Task deleteTaskById(Long taskId) {
        Task task = findTaskById(taskId);
        weekCardService.removeTaskFromWeekCard(taskId, task.getWeekCardId());
        taskRepository.deleteById(taskId);
        return task;
    }

    public void verifyUserAuthorizationForTask(Task task) {
        weekCardService.verifyUserAuthorizationForWeekCard(task.getWeekCardId());
    }

    private void addTaskToWeekCard(Long weekCardId, Long taskId) {
        WeekCard weekCard = weekCardService.findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().add(taskId);
        weekCardRepository.save(weekCard);
    }

}
