package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.NULL_FIELD;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.TASK_NOT_FOUND_BY_ID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeekCardService weekCardService;

    public TaskService(
            TaskRepository taskRepository,
            WeekCardService weekCardService
    ) {
        this.taskRepository = taskRepository;
        this.weekCardService = weekCardService;
    }

    public Task create(TaskDTO taskDTO) {
        verifyDtoNullFields(taskDTO);
        weekCardService.verifyUserAuthorizationForWeekCard(taskDTO.weekCardId());
        Task task = new Task(taskDTO);
        task = taskRepository.save(task);
        weekCardService.addTask(task.getId(), task.getWeekCardId());
        return task;
    }

    public Task findById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND_BY_ID.params(taskId.toString()).getMessage()));
        verifyUserAuthorizationForTask(task);
        return task;
    }

    public List<Task> findByWeekCardId(Long weekCardId) {
        weekCardService.verifyUserAuthorizationForWeekCard(weekCardId);
        return taskRepository.findTasksByWeekCardId(weekCardId);
    }

    public Task updateTaskById(Long taskId, TaskDTO taskDTO) {
        Task task = findById(taskId);
        if (taskDTO.weekCardId() != null) {
            weekCardService.addTask(taskId, taskDTO.weekCardId());
            weekCardService.removeTask(taskId, task.getWeekCardId());
            task.setWeekCardId(taskDTO.weekCardId());
        }
        if (taskDTO.title() != null) task.setTitle(taskDTO.title());
        if (taskDTO.description() != null) task.setDescription(taskDTO.description());
        if (taskDTO.dueDate() != null) task.setDueDate(taskDTO.dueDate());
        return taskRepository.save(task);
    }

    public Task inProgress(Long taskId) {
        Task task = findById(taskId);
        task.setStatus(Task.TaskStatus.IN_PROGRESS);
        return taskRepository.save(task);
    }

    public Task complete(Long taskId) {
        Task task = findById(taskId);
        task.setStatus(Task.TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }

    public Task deleteById(Long taskId) {
        Task task = findById(taskId);
        weekCardService.removeTask(taskId, task.getWeekCardId());
        taskRepository.deleteById(taskId);
        return task;
    }

    public void verifyUserAuthorizationForTask(Task task) {
        weekCardService.verifyUserAuthorizationForWeekCard(task.getWeekCardId());
    }

    private void verifyDtoNullFields(TaskDTO dto) {
        weekCardService.findById(dto.weekCardId());
        if(dto.weekCardId() == null)
            throw new NullFieldException(NULL_FIELD.params("'weekCardId'").getMessage());
        if(dto.title() == null)
            throw new NullFieldException(NULL_FIELD.params("'title'").getMessage());
        if(dto.description() == null)
            throw new NullFieldException(NULL_FIELD.params("'description'").getMessage());
    }

}
