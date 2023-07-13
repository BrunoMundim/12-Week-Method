package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.dto.update.UpdateGoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.KeyResult;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.service.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.mundim.WeekMethod.entity.Goal.StatusType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalControllerTest {

    private static final Long userId = 1L;
    private static final Long goalId = 1L;
    private static final String userEmail = "email@email.com";
    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";
    private static Goal goal;
    private static GoalDTO goalDTO;
    private static User user;
    private static KeyResult keyResult1;
    private static KeyResult keyResult2;
    private static List<String> keyResults;

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalController goalController;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(userId).name("name").email(userEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        keyResults = Arrays.asList("Key Result 1", "Key Result 2");
        keyResult1 = KeyResult.builder()
                .id(1L).completed(false).description("Key Result 1")
                .build();
        keyResult2 = KeyResult.builder()
                .id(2L).completed(false).description("Key Result 1")
                .build();
        goal = Goal.builder()
                .id(goalId).userId(user.getId()).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
        goalDTO = GoalDTO.builder()
                .title("Title").description("Description")
                .keyResultsDescription(keyResults)
                .build();
    }

    @Test
    public void createGoal_shouldReturnCreatedGoal() {
        when(goalService.createGoalForLoggedUser(Mockito.any(GoalDTO.class)))
                .thenReturn(goal);

        ResponseEntity<Goal> createdGoal = goalController.createGoal(goalDTO);

        assertThat(createdGoal).isNotNull();
        assertThat(createdGoal.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdGoal.getBody()).isEqualTo(goal);
    }

    @Test
    public void findGoalById_shouldReturnFoundGoal() {
        when(goalService.findById(Mockito.any(Long.class)))
                .thenReturn(goal);

        ResponseEntity<Goal> foundGoal = goalController.findGoalById(goal.getId());

        assertThat(foundGoal).isNotNull();
        assertThat(foundGoal.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundGoal.getBody()).isEqualTo(goal);
    }

    @Test
    public void findGoalsByUserId_shouldReturnFoundGoals() {
        Goal goal2 = Goal.builder()
                .id(2L).userId(user.getId()).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
        List<Goal> goals = Arrays.asList(goal, goal2);
        when(goalService.findByUserId(Mockito.any(Long.class)))
                .thenReturn(goals);

        ResponseEntity<List<Goal>> foundGoals = goalController.findGoalsByUserId(goal.getUserId());

        assertThat(foundGoals).isNotNull();
        assertThat(foundGoals.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundGoals.getBody()).isEqualTo(goals);
    }

    @Test
    public void findGoalsFromLoggedUser_shouldReturnFoundGoals() {
        Goal goal2 = Goal.builder()
                .id(2L).userId(user.getId()).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
        List<Goal> goals = Arrays.asList(goal, goal2);
        when(goalService.findFromLoggedUser())
                .thenReturn(goals);

        ResponseEntity<List<Goal>> foundGoals = goalController.findGoalsFromLoggedUser();

        assertThat(foundGoals).isNotNull();
        assertThat(foundGoals.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundGoals.getBody()).isEqualTo(goals);
    }

    @Test
    public void updateGoalById_shouldReturnUpdatedGoal() {
        UpdateGoalDTO updateGoalDTO = UpdateGoalDTO.builder()
                .title("new title").description("new description")
                .keyResultsDescription(keyResults)
                .build();
        Goal updatedGoal = Goal.builder()
                .id(goalId).userId(user.getId()).title("new title").description("new description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
        when(goalService.updateGoal(goal.getId(), updateGoalDTO))
                .thenReturn(updatedGoal);

        ResponseEntity<Goal> updateGoalResponse = goalController.updateGoalById(goal.getId(), updateGoalDTO);

        assertThat(updateGoalResponse).isNotNull();
        assertThat(updateGoalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateGoalResponse.getBody()).isEqualTo(updatedGoal);
    }

    @Test
    public void inProgressGoal_shouldReturnInProgressGoal() {
        goal.setStatus(IN_PROGRESS);
        when(goalService.inProgressGoal(goal.getId()))
                .thenReturn(goal);

        ResponseEntity<Goal> updateGoalResponse = goalController.inProgressGoal(goal.getId());

        assertThat(updateGoalResponse).isNotNull();
        assertThat(updateGoalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateGoalResponse.getBody()).isEqualTo(goal);
        assertThat(updateGoalResponse.getBody().getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    public void completeGoal_shouldReturnCompletedGoal() {
        goal.setStatus(COMPLETED);
        when(goalService.completeGoal(goal.getId()))
                .thenReturn(goal);

        ResponseEntity<Goal> updateGoalResponse = goalController.completeGoal(goal.getId());

        assertThat(updateGoalResponse).isNotNull();
        assertThat(updateGoalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateGoalResponse.getBody()).isEqualTo(goal);
        assertThat(updateGoalResponse.getBody().getStatus()).isEqualTo(COMPLETED);
    }

    @Test
    public void completeKeyResult_shouldReturnCompletedKeyResultGoal() {
        KeyResult keyResult = goal.getKeyResults().get(0);
        keyResult.setCompleted(true);
        when(goalService.completeKeyResult(goal.getId(), keyResult.getId()))
                .thenReturn(goal);

        ResponseEntity<Goal> updateGoalResponse = goalController.completeKeyResult(goal.getId(), keyResult.getId());

        assertThat(updateGoalResponse).isNotNull();
        assertThat(updateGoalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateGoalResponse.getBody()).isEqualTo(goal);
        assertThat(updateGoalResponse.getBody().getKeyResults().get(0).isCompleted()).isTrue();
    }

    @Test
    public void uncompleteKeyResult_shouldReturnUncompletedKeyResultGoal() {
        KeyResult keyResult = goal.getKeyResults().get(0);
        keyResult.setCompleted(false);
        when(goalService.uncompleteKeyResult(goal.getId(), keyResult.getId()))
                .thenReturn(goal);

        ResponseEntity<Goal> updateGoalResponse = goalController.uncompleteKeyResult(goal.getId(), keyResult.getId());

        assertThat(updateGoalResponse).isNotNull();
        assertThat(updateGoalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateGoalResponse.getBody()).isEqualTo(goal);
        assertThat(updateGoalResponse.getBody().getKeyResults().get(0).isCompleted()).isFalse();
    }

    @Test
    public void deleteGoalById_shouldReturnDeletedGoal() {
        when(goalService.deleteById(goal.getId()))
                .thenReturn(goal);

        ResponseEntity<Goal> deletedGoal = goalController.deleteGoalById(goal.getId());

        assertThat(deletedGoal).isNotNull();
        assertThat(deletedGoal.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deletedGoal.getBody()).isEqualTo(goal);
    }

}
