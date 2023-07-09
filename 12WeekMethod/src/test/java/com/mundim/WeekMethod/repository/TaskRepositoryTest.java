package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.entity.WeekCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.mundim.WeekMethod.entity.Task.TaskStatus.IN_PROGRESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static Long weekCardId;

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final WeekCardRepository weekCardRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskRepositoryTest(UserRepository userRepository, GoalRepository goalRepository, WeekCardRepository weekCardRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.weekCardRepository = weekCardRepository;
        this.taskRepository = taskRepository;
    }

    @BeforeEach
    public void setup() {
        User user = User.builder()
                .name("name").email("email1@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();
        userRepository.save(user);
        Goal goal = Goal.builder()
                .userId(user.getId()).status(Goal.StatusType.IN_PROGRESS).build();
        goalRepository.save(goal);
        WeekCard weekCard = WeekCard.builder()
                .goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        weekCardRepository.save(weekCard);
        weekCardId = weekCard.getId();
    }

    @Test
    public void save_shouldReturnSavedTask() {
        Task task = Task.builder()
                .weekCardId(weekCardId).title("title").description("description")
                .dueDate(LocalDate.now()).status(IN_PROGRESS)
                .build();

        Task savedTask = taskRepository.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnNotNullTask() {
        Task task = Task.builder()
                .weekCardId(weekCardId).title("title").description("description")
                .dueDate(LocalDate.now()).status(IN_PROGRESS)
                .build();
        taskRepository.save(task);

        Task foundTask = taskRepository.findById(task.getId()).orElse(null);

        assertThat(foundTask).isNotNull();
    }

    @Test
    public void findByWeekCardId_shouldReturnTwoTasks() {
        Task task1 = Task.builder()
                .weekCardId(weekCardId).title("title").description("description")
                .dueDate(LocalDate.now()).status(IN_PROGRESS)
                .build();
        Task task2 = Task.builder()
                .weekCardId(weekCardId).title("title").description("description")
                .dueDate(LocalDate.now()).status(IN_PROGRESS)
                .build();
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> foundTasks = taskRepository.findTasksByWeekCardId(weekCardId);

        assertThat(foundTasks).isNotNull();
        assertThat(foundTasks.size()).isEqualTo(2);
    }

    @Test
    public void deleteById_shouldSetTaskToNull() {
        Task task = Task.builder()
                .weekCardId(weekCardId).title("title").description("description")
                .dueDate(LocalDate.now()).status(IN_PROGRESS)
                .build();
        taskRepository.save(task);

        taskRepository.deleteById(task.getId());
        Task foundTask = taskRepository.findById(task.getId()).orElse(null);

        assertThat(foundTask).isNull();
    }

}