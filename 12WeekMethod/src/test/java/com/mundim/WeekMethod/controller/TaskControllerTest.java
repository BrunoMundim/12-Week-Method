package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.*;
import com.mundim.WeekMethod.repository.TaskRepository;
import com.mundim.WeekMethod.service.TaskService;
import com.mundim.WeekMethod.service.WeekCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.mundim.WeekMethod.entity.Task.TaskStatus.NOT_STARTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private static User user;
    private static Goal goal;
    private static WeekCard weekCard;
    private static Task task;
    private static TaskDTO taskDTO;
    private static final Long userId = 1L;
    private static final Long goalId = 1L;
    private static final String userEmail = "email@email.com";
    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(userId).name("name").email(userEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        List<String> keyResults = Arrays.asList("Key Result 1", "Key Result 2");
        KeyResult keyResult1 = KeyResult.builder()
                .id(1L).completed(false).description("Key Result 1")
                .build();
        KeyResult keyResult2 = KeyResult.builder()
                .id(2L).completed(false).description("Key Result 1")
                .build();
        goal = Goal.builder()
                .id(goalId).userId(user.getId()).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(Goal.StatusType.NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
        weekCard = WeekCard.builder()
                .id(1L).goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<>()).notes("notes")
                .build();
        task = Task.builder()
                .id(1L).weekCardId(weekCard.getId()).title("title").description("description")
                .dueDate(LocalDate.now().plusDays(3)).status(NOT_STARTED)
                .build();
        taskDTO = TaskDTO.builder()
                .weekCardId(weekCard.getId()).title("title").description("description")
                .dueDate(LocalDate.now().plusDays(3))
                .build();
    }

    @Test
    public void createTask_shouldReturnCreatedTask() {
        when(taskService.create(taskDTO)).thenReturn(task);

        ResponseEntity<Task> createdTask = taskController.createTask(taskDTO);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdTask.getBody()).isEqualTo(task);
    }

    @Test
    public void findTaskById_shouldReturnFoundTask() {
        when(taskService.findById(task.getId())).thenReturn(task);

        ResponseEntity<Task> foundTask = taskController.findTaskById(task.getId());

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundTask.getBody()).isEqualTo(task);
    }

    @Test
    public void findTasksByWeekCardId_shouldReturnFoundTasks() {
        when(taskService.findByWeekCardId(task.getWeekCardId())).thenReturn(Arrays.asList(task, task));

        ResponseEntity<List<Task>> foundTasks = taskController.findTasksByWeekCardId(task.getWeekCardId());

        assertThat(foundTasks).isNotNull();
        assertThat(foundTasks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundTasks.getBody()).isEqualTo(Arrays.asList(task, task));
    }

    @Test
    public void updateTaskById_shouldReturnUpdatedTask() {
        when(taskService.updateTaskById(task.getId(), taskDTO)).thenReturn(task);

        ResponseEntity<Task> updatedTask = taskController.updateTaskById(task.getId(), taskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedTask.getBody()).isEqualTo(task);
    }

    @Test
    public void inProgressTask_shouldReturnUpdatedTask() {
        when(taskService.inProgress(task.getId())).thenReturn(task);

        ResponseEntity<Task> updatedTask = taskController.inProgressTask(task.getId());

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedTask.getBody()).isEqualTo(task);
    }

    @Test
    public void deleteTaskById_shouldReturnDeletedTask() {
        when(taskService.deleteById(task.getId())).thenReturn(task);

        ResponseEntity<Task> deletedTask = taskController.deleteTaskById(task.getId());

        assertThat(deletedTask).isNotNull();
        assertThat(deletedTask.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deletedTask.getBody()).isEqualTo(task);
    }

}
