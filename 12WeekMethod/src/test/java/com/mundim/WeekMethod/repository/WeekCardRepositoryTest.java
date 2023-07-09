package com.mundim.WeekMethod.repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.entity.WeekCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class WeekCardRepositoryTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static Long userId;
    private static Long goalId;
    private static Long goalUserId;

    WeekCardRepository weekCardRepository;
    UserRepository userRepository;
    GoalRepository goalRepository;

    @Autowired
    public WeekCardRepositoryTest(
            WeekCardRepository weekCardRepository,
            UserRepository userRepository,
            GoalRepository goalRepository
    ) {
        this.weekCardRepository = weekCardRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
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
        userId = user.getId();
        goalId = goal.getId();
        goalUserId = goal.getUserId();
    }

    @Test
    public void save_ShouldReturnSavedWeekCard() {
        WeekCard weekCard = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();

        WeekCard savedWeekCard = weekCardRepository.save(weekCard);

        assertThat(savedWeekCard).isNotNull();
        assertThat(savedWeekCard.getId()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnNonNullWeekCard() {
        WeekCard weekCard = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        weekCardRepository.save(weekCard);

        WeekCard foundWeekCard = weekCardRepository.findById(weekCard.getId()).orElse(null);

        assertThat(foundWeekCard).isNotNull();
        assertThat(weekCard.getGoalId()).isNotNull();
        assertThat(goalUserId).isNotNull();
    }

    @Test
    public void findByGoalId_shouldReturnTwoWeekCards() {
        WeekCard weekCard1 = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        WeekCard weekCard2 = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        weekCardRepository.save(weekCard1);
        weekCardRepository.save(weekCard2);

        List<WeekCard> foundWeekCards = weekCardRepository.findWeekCardsByGoalId(goalId);

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards.size()).isEqualTo(2);
    }

    @Test
    public void deleteById_shouldSetWeekCardToNull() {
        WeekCard weekCard = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        weekCardRepository.save(weekCard);

        weekCardRepository.deleteById(weekCard.getId());
        WeekCard foundWeekCard = weekCardRepository.findById(weekCard.getId()).orElse(null);

        assertThat(foundWeekCard).isNull();
    }

    @Test
    public void delete_shouldSetWeekCardToNull() {
        WeekCard weekCard = WeekCard.builder()
                .goalId(goalId).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<Long>()).notes("Notes")
                .build();
        weekCardRepository.save(weekCard);

        weekCardRepository.delete(weekCard);
        WeekCard foundWeekCard = weekCardRepository.findById(weekCard.getId()).orElse(null);

        assertThat(foundWeekCard).isNull();
    }

}
