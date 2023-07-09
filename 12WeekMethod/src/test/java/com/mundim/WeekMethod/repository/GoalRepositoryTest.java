package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.KeyResult;
import com.mundim.WeekMethod.entity.Task;
import com.mundim.WeekMethod.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mundim.WeekMethod.entity.Goal.StatusType.NOT_STARTED;
import static com.mundim.WeekMethod.entity.Task.TaskStatus.IN_PROGRESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GoalRepositoryTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static Long userId;

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    @Autowired
    public GoalRepositoryTest(UserRepository userRepository, GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
    }

    @BeforeEach
    public void setup() {
        User user = User.builder()
                .name("name").email("email1@email.com").password("password")
                .registrationDate(LocalDate.now()).role(ROLE_USER).build();
        userRepository.save(user);
        userId = user.getId();
    }

    @Test
    public void save_shouldReturnSavedGoal() {
        Goal goal = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal.getKeyResults().add(new KeyResult("Key Result 1"));
        goal.getKeyResults().add(new KeyResult("Key Result 2"));

        Goal savedGoal = goalRepository.save(goal);

        assertThat(savedGoal).isNotNull();
        assertThat(savedGoal.getId()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnNotNullGoal() {
        Goal goal = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal.getKeyResults().add(new KeyResult("Key Result 1"));
        goal.getKeyResults().add(new KeyResult("Key Result 2"));
        goalRepository.save(goal);

        Goal foundGoal = goalRepository.findById(goal.getId()).orElse(null);

        assertThat(foundGoal).isNotNull();
    }

    @Test
    public void findByUserId_shouldReturnTwoGoals() {
        Goal goal1 = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal1.getKeyResults().add(new KeyResult("Key Result 1"));
        goal1.getKeyResults().add(new KeyResult("Key Result 2"));
        Goal goal2 = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal2.getKeyResults().add(new KeyResult("Key Result 1"));
        goal2.getKeyResults().add(new KeyResult("Key Result 2"));
        goalRepository.save(goal1);
        goalRepository.save(goal2);

        List<Goal> goals = goalRepository.findGoalsByUserId(userId);

        assertThat(goals).isNotNull();
        assertThat(goals.size()).isEqualTo(2);
    }

    @Test
    public void deleteById_shouldSetGoalToNull() {
        Goal goal = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal.getKeyResults().add(new KeyResult("Key Result 1"));
        goal.getKeyResults().add(new KeyResult("Key Result 2"));
        goalRepository.save(goal);

        goalRepository.deleteById(goal.getId());
        Goal foundGoal = goalRepository.findById(goal.getId()).orElse(null);

        assertThat(foundGoal).isNull();
    }

    @Test
    public void delete_shouldSetGoalToNull() {
        Goal goal = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        goal.getKeyResults().add(new KeyResult("Key Result 1"));
        goal.getKeyResults().add(new KeyResult("Key Result 2"));
        goalRepository.save(goal);

        goalRepository.delete(goal);
        Goal foundGoal = goalRepository.findById(goal.getId()).orElse(null);

        assertThat(foundGoal).isNull();
    }

}
