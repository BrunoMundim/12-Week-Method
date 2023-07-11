package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.dto.update.UpdateGoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.KeyResult;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.UnauthorizedRequestException;
import com.mundim.WeekMethod.repository.GoalRepository;
import com.mundim.WeekMethod.repository.KeyResultRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mundim.WeekMethod.entity.Goal.StatusType.*;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.GOAL_NOT_FOUND_BY_ID;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.UNAUTHORIZED_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {

    private static final Long userId = 1L;
    private static final Long goalId = 1L;
    private static final String userEmail = "email@email.com";
    private static final String unauthorizedUserEmail = "unauthorizedemail@email.com";
    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";
    private static Goal goal;
    private static GoalDTO goalDTO;
    private static User user;
    private static User unauthorizedUser;
    private static KeyResult keyResult1;
    private static KeyResult keyResult2;

    @Mock
    private GoalRepository goalRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private MailService mailService;
    @Mock
    private KeyResultRepository keyResultRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(userId).name("name").email(userEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        List<String> keyResults = Arrays.asList("Key Result 1", "Key Result 2");
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
        unauthorizedUser = User.builder()
                .id(2L).name("name").email(unauthorizedUserEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
    }

    @Test
    public void create_shouldReturnCreatedGoal() {
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal createdGoal = goalService.createGoalForLoggedUser(goalDTO);

        assertThat(createdGoal).isNotNull();
        assertThat(createdGoal.getUserId()).isEqualTo(userId);
    }

    @Test
    public void findById_shouldReturnFoundGoal() {
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal foundGoal = goalService.findById(goalId);

        assertThat(foundGoal).isNotNull();
    }

    @Test
    public void findById_shouldThrownGoalNotFound() {
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> goalService.findById(goalId));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage(GOAL_NOT_FOUND_BY_ID.params(goalId.toString()).getMessage());
    }

    @Test
    public void findById_shouldThrownUnauthorizedUser() {
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(unauthorizedUser);

        Throwable thrown = catchThrowable(() -> goalService.findById(goalId));

        assertThat(thrown).isInstanceOf(UnauthorizedRequestException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }

    @Test
    public void findByUserId_shouldReturnListOfTwoGoals() {
        Goal goal2 = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        List<Goal> expectedGoals = Arrays.asList(goal, goal2);
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.findGoalsByUserId(Mockito.any(Long.class))).thenReturn(expectedGoals);

        List<Goal> goals = goalService.findByUserId(user.getId());

        assertThat(goals).isNotNull();
        assertThat(goals).isEqualTo(expectedGoals);
        assertThat(goals.size()).isEqualTo(2);
    }

    @Test
    public void findByUserId_shouldThrowUnauthorizedUser() {
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Throwable throwable = catchThrowable(() -> goalService.findByUserId(unauthorizedUser.getId()));

        assertThat(throwable).isInstanceOf(UnauthorizedRequestException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }

    @Test
    public void findFromLoggedUser_shouldReturnListOfTwoGoals() {
        Goal goal2 = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        List<Goal> expectedGoals = Arrays.asList(goal, goal2);
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.findGoalsByUserId(user.getId())).thenReturn(expectedGoals);

        List<Goal> goals = goalService.findFromLoggedUser();

        assertThat(goals).isNotNull();
        assertThat(goals).isEqualTo(expectedGoals);
        assertThat(goals.size()).isEqualTo(2);
    }

    @Test
    public void updateGoal_shouldReturnUpdatedGoalTitle() {
        final String newTitle = "New title";
        UpdateGoalDTO updatedGoalDTO = UpdateGoalDTO.builder()
                .title(newTitle).description(null)
                .keyResultsDescription(null)
                .build();
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);

        Goal updatedGoal = goalService.updateGoal(goal.getId(), updatedGoalDTO);

        assertThat(updatedGoal).isNotNull();
        assertThat(updatedGoal.getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void updateGoal_shouldReturnUpdatedGoalDescription() {
        final String newDescription = "New description";
        UpdateGoalDTO updatedGoalDTO = UpdateGoalDTO.builder()
                .title(null).description(newDescription)
                .keyResultsDescription(null)
                .build();
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);

        Goal updatedGoal = goalService.updateGoal(goal.getId(), updatedGoalDTO);

        assertThat(updatedGoal).isNotNull();
        assertThat(updatedGoal.getDescription()).isEqualTo(newDescription);
    }

    @Test
    public void updateGoal_shouldReturnUpdatedKeyResults() {
        final List<KeyResult> keyResults = Arrays
                .asList(new KeyResult("Key 1"), new KeyResult("Key 2"));
        UpdateGoalDTO updatedGoalDTO = UpdateGoalDTO.builder()
                .title(null).description(null)
                .keyResultsDescription(Arrays.asList("Key 1", "Key 2"))
                .build();
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);

        Goal updatedGoal = goalService.updateGoal(goal.getId(), updatedGoalDTO);

        assertThat(updatedGoal).isNotNull();
        assertThat(updatedGoal.getKeyResults()).isEqualTo(keyResults);
    }

    @Test
    public void updateGoal_shouldThrowNotFoundGoal() {
        UpdateGoalDTO updatedGoalDTO = UpdateGoalDTO.builder()
                .title(null).description(null)
                .keyResultsDescription(Arrays.asList("Key 1", "Key 2"))
                .build();
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> goalService.updateGoal(goalId, updatedGoalDTO));

        assertThat(throwable).isInstanceOf(BadRequestException.class)
                .hasMessage(GOAL_NOT_FOUND_BY_ID.params(goal.getId().toString()).getMessage());
    }

    @Test
    public void updateGoal_shouldThrowUnauthorizedGoal() {
        UpdateGoalDTO updatedGoalDTO = UpdateGoalDTO.builder()
                .title(null).description(null)
                .keyResultsDescription(Arrays.asList("Key 1", "Key 2"))
                .build();
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(unauthorizedUser);

        Throwable throwable = catchThrowable(() -> goalService.updateGoal(goalId, updatedGoalDTO));

        assertThat(throwable).isInstanceOf(UnauthorizedRequestException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }

    @Test
    public void deleteById_shouldReturnDeletedGoal() {
        when(goalRepository.findById(goal.getId())).thenReturn(Optional.ofNullable(goal));
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal deletedGoal = goalService.deleteById(goal.getId());

        assertThat(deletedGoal).isNotNull();
        assertThat(deletedGoal).isEqualTo(goal);
    }

    @Test
    public void deleteById_shouldThrowUnauthorizedUser() {
        when(authenticationService.findUserByBearer()).thenReturn(unauthorizedUser);

        Throwable throwable = catchThrowable(() -> goalService.deleteByUserId(goal.getId()));

        assertThat(throwable).isInstanceOf(UnauthorizedRequestException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }

    @Test
    public void deleteByUserId_shouldReturnListOfTwoDeletedGoals() {
        Goal goal2 = Goal.builder()
                .userId(userId).title("title").description("description")
                .startDate(null).endDate(null).completionDate(null)
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(new ArrayList<>())
                .build();
        List<Goal> expectedGoals = Arrays.asList(goal, goal2);
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(goalRepository.findGoalsByUserId(Mockito.any(Long.class))).thenReturn(expectedGoals);

        List<Goal> goals = goalService.deleteByUserId(goal.getUserId());

        assertThat(goals).isNotNull();
        assertThat(goals).isEqualTo(expectedGoals);
        assertThat(goals.size()).isEqualTo(2);
    }

    @Test
    public void deleteByUserId_shouldThrowUnauthorizedUser() {
        when(authenticationService.findUserByBearer()).thenReturn(unauthorizedUser);

        Throwable throwable = catchThrowable(() -> goalService.deleteByUserId(goal.getUserId()));

        assertThat(throwable).isInstanceOf(UnauthorizedRequestException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }

    @Test
    public void inProgressGoal_shouldChangeStatusToInProgress() {
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal foundGoal = goalService.inProgressGoal(goalId);

        assertThat(foundGoal).isNotNull();
        assertThat(foundGoal.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    public void completeGoal_shouldChangeStatusToCompleted() {
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal foundGoal = goalService.completeGoal(goalId);

        assertThat(foundGoal).isNotNull();
        assertThat(foundGoal.getStatus()).isEqualTo(COMPLETED);
    }

    @Test
    public void completeKeyResult_shouldChangeKeyResultStatusToCompleted() {
        KeyResult keyResult = goal.getKeyResults().get(0);
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);
        when(keyResultRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(keyResult1));
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal foundGoal = goalService.inProgressGoal(goalId);
        foundGoal = goalService.completeKeyResult(goalId, keyResult.getId());

        assertThat(keyResult).isNotNull();
        assertThat(keyResult.isCompleted()).isTrue();
        assertThat(foundGoal.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    public void uncompleteKeyResult_shouldChangeKeyResultStatusToUncompleted() {
        KeyResult keyResult = goal.getKeyResults().get(0);
        when(goalRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(goal));
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);
        when(keyResultRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(keyResult1));
        when(authenticationService.findUserByBearer()).thenReturn(user);

        Goal foundGoal = goalService.inProgressGoal(goalId);
        foundGoal = goalService.completeKeyResult(goalId, keyResult.getId());
        foundGoal = goalService.uncompleteKeyResult(goalId, keyResult.getId());

        assertThat(keyResult).isNotNull();
        assertThat(keyResult.isCompleted()).isFalse();
        assertThat(foundGoal.getStatus()).isEqualTo(IN_PROGRESS);
    }

}
