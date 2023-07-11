package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.TaskDTO;
import com.mundim.WeekMethod.entity.*;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.mundim.WeekMethod.entity.Task.TaskStatus.*;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.NULL_FIELD;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.TASK_NOT_FOUND_BY_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

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
    private TaskRepository taskRepository;
    @Mock
    private WeekCardService weekCardService;

    @InjectMocks
    private TaskService taskService;

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
    public void create_shouldReturnCreatedTask() {
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task foundTask = taskService.create(taskDTO);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask).isEqualTo(task);
    }

    @Test
    public void create_shouldThrowNullFieldWeekCardId() {
        taskDTO = TaskDTO.builder()
                .weekCardId(null).title("title").description("description")
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        Throwable throwable = catchThrowable(() -> taskService.create(taskDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'weekCardId'").getMessage());
    }

    @Test
    public void create_shouldThrowNullFieldTitle() {
        taskDTO = TaskDTO.builder()
                .weekCardId(weekCard.getId()).title(null).description("description")
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        Throwable throwable = catchThrowable(() -> taskService.create(taskDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'title'").getMessage());
    }

    @Test
    public void create_shouldThrowNullFieldDescription() {
        taskDTO = TaskDTO.builder()
                .weekCardId(weekCard.getId()).title("title").description(null)
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        Throwable throwable = catchThrowable(() -> taskService.create(taskDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'description'").getMessage());
    }

    @Test
    public void findById_shouldReturnFoundTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));

        Task foundTask = taskService.findById(task.getId());

        assertThat(foundTask).isEqualTo(task);
    }

    @Test
    public void findById_shouldThrowNotFoundTask() {
        Throwable throwable = catchThrowable(() -> taskService.findById(2L));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(TASK_NOT_FOUND_BY_ID.params(String.valueOf(2L)).getMessage());
    }

    @Test
    public void findByWeekCardId_shouldReturnFoundTask() {
        List<Task> tasks = Arrays.asList(task, task);
        when(taskRepository.findTasksByWeekCardId(task.getWeekCardId())).thenReturn(tasks);

        List<Task> foundTasks = taskService.findByWeekCardId(task.getId());

        assertThat(foundTasks).isEqualTo(tasks);
        assertThat(foundTasks.size()).isEqualTo(2);
    }

    @Test
    public void updateTaskById_shouldReturnUpdatedTaskWeekCardId() {
        TaskDTO updateTaskDTO = TaskDTO.builder()
                .weekCardId(2L).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskById(task.getId(), updateTaskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getWeekCardId()).isEqualTo(2L);
        assertThat(updatedTask.getTitle()).isNotNull();
        assertThat(updatedTask.getDescription()).isNotNull();
        assertThat(updatedTask.getDueDate()).isNotNull();
    }

    @Test
    public void updateTaskById_shouldReturnUpdatedTaskTitle() {
        TaskDTO updateTaskDTO = TaskDTO.builder()
                .title("new title").build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskById(task.getId(), updateTaskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getWeekCardId()).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("new title");
        assertThat(updatedTask.getDescription()).isNotNull();
        assertThat(updatedTask.getDueDate()).isNotNull();
    }

    @Test
    public void updateTaskById_shouldReturnUpdatedTaskDescription() {
        TaskDTO updateTaskDTO = TaskDTO.builder()
                .description("new description").build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskById(task.getId(), updateTaskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getWeekCardId()).isNotNull();
        assertThat(updatedTask.getTitle()).isNotNull();
        assertThat(updatedTask.getDescription()).isEqualTo("new description");
        assertThat(updatedTask.getDueDate()).isNotNull();
    }

    @Test
    public void updateTaskById_shouldReturnUpdatedTaskDueDate() {
        TaskDTO updateTaskDTO = TaskDTO.builder()
                .dueDate(LocalDate.now().plusMonths(1)).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskById(task.getId(), updateTaskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getWeekCardId()).isNotNull();
        assertThat(updatedTask.getTitle()).isNotNull();
        assertThat(updatedTask.getDescription()).isNotNull();
        assertThat(updatedTask.getDueDate()).isEqualTo(LocalDate.now().plusMonths(1));
    }

    @Test
    public void inProgress_shouldChangeStatusToInProgress() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.inProgress(task.getId());

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    public void complete_shouldChangeStatusToComplete() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.complete(task.getId());

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(COMPLETED);
    }

    @Test
    public void deleteById_shouldReturnDeletedTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));

        Task deletedTask = taskService.deleteById(task.getId());

        assertThat(deletedTask).isNotNull();
    }
}
