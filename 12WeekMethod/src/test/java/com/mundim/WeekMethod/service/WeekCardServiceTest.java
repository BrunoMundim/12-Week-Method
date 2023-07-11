package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.dto.update.UpdateWeekCardDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.KeyResult;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.GoalRepository;
import com.mundim.WeekMethod.repository.WeekCardRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static com.mundim.WeekMethod.entity.Goal.StatusType.NOT_STARTED;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.NULL_FIELD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeekCardServiceTest {

    private static User user;
    private static Goal goal;
    private static KeyResult keyResult1;
    private static KeyResult keyResult2;
    private static User unauthorizedUser;
    private static WeekCard weekCard;
    private static WeekCardDTO weekCardDTO;
    private static final Long userId = 1L;
    private static final Long goalId = 1L;
    private static final String userEmail = "email@email.com";
    private static final String unauthorizedUserEmail = "unauthorizedemail@email.com";
    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";

    @Mock
    private WeekCardRepository weekCardRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private GoalService goalService;
    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private WeekCardService weekCardService;

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
        unauthorizedUser = User.builder()
                .id(2L).name("name").email(unauthorizedUserEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();

        weekCard = WeekCard.builder()
                .id(1L).goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<>()).notes("notes")
                .build();
        weekCardDTO = WeekCardDTO.builder()
                .goalId(goal.getId()).description("description").weekStartDate(LocalDate.now()).notes("notes")
                .build();
    }

    @Test
    public void createForLoggedUser_shouldReturnCreatedWeekCard() {
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);

        WeekCard createdWeekCard = weekCardService.createForLoggedUser(weekCardDTO);

        assertThat(createdWeekCard).isNotNull();
        assertThat(createdWeekCard).isEqualTo(weekCard);
    }

    @Test
    public void createForLoggedUser_shouldThrowNullFieldExceptionGoalId() {
        weekCardDTO = WeekCardDTO.builder()
                .goalId(null).description("description").weekStartDate(LocalDate.now()).notes("notes")
                .build();

        Throwable throwable = catchThrowable(() -> weekCardService.createForLoggedUser(weekCardDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'goalId'").getMessage());
    }

    @Test
    public void createForLoggedUser_shouldThrowNullFieldExceptionDescription() {
        weekCardDTO = WeekCardDTO.builder()
                .goalId(goal.getId()).description(null).weekStartDate(LocalDate.now()).notes("notes")
                .build();

        Throwable throwable = catchThrowable(() -> weekCardService.createForLoggedUser(weekCardDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'description'").getMessage());
    }

    @Test
    public void createForLoggedUser_shouldThrowNullFieldExceptionWeekStartDate() {
        weekCardDTO = WeekCardDTO.builder()
                .goalId(goal.getId()).description("description").weekStartDate(null).notes("notes")
                .build();

        Throwable throwable = catchThrowable(() -> weekCardService.createForLoggedUser(weekCardDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'weekStartDate'").getMessage());
    }

    @Test
    public void createForLoggedUser_shouldThrowNullFieldExceptionNotes() {
        weekCardDTO = WeekCardDTO.builder()
                .goalId(goal.getId()).description("description").weekStartDate(LocalDate.now()).notes(null)
                .build();

        Throwable throwable = catchThrowable(() -> weekCardService.createForLoggedUser(weekCardDTO));

        assertThat(throwable).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.params("'notes'").getMessage());
    }

    @Test
    public void findByGoalId_shouldReturnListOfTwoWeekCards() {
        WeekCard weekCard2 = WeekCard.builder()
                .id(2L).goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<>()).notes("notes")
                .build();
        List<WeekCard> weekCards = Arrays.asList(weekCard, weekCard2);
        when(weekCardRepository.findWeekCardsByGoalId(Mockito.any(Long.class))).thenReturn(weekCards);

        List<WeekCard> foundWeekCards = weekCardService.findByGoalId(goal.getId());

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards).isEqualTo(weekCards);
        assertThat(foundWeekCards.size()).isEqualTo(2);
    }

    @Test
    public void findWeekCardsByUserId_shouldReturnListOfTwoWeekCards() {
        WeekCard weekCard2 = WeekCard.builder()
                .id(2L).goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<>()).notes("notes")
                .build();
        List<WeekCard> weekCards = Arrays.asList(weekCard, weekCard2);
        when(goalService.findByUserId(Mockito.any(Long.class))).thenReturn(Collections.singletonList(goal));
        when(weekCardRepository.findWeekCardsByGoalId(Mockito.any(Long.class))).thenReturn(weekCards);

        List<WeekCard> foundWeekCards = weekCardService.findByUserId(goal.getUserId());

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards).isEqualTo(weekCards);
        assertThat(foundWeekCards.size()).isEqualTo(2);
    }

    @Test
    public void findWeekCardsForLoggedUser_shouldReturnListOfTwoWeekCards() {
        WeekCard weekCard2 = WeekCard.builder()
                .id(2L).goalId(goal.getId()).description("description")
                .weekStartDate(LocalDate.now()).weekEndDate(LocalDate.now().plusDays(7))
                .weekTasksIds(new HashSet<>()).notes("notes")
                .build();
        List<WeekCard> weekCards = Arrays.asList(weekCard, weekCard2);
        when(goalService.findByUserId(Mockito.any(Long.class))).thenReturn(Collections.singletonList(goal));
        when(weekCardRepository.findWeekCardsByGoalId(Mockito.any(Long.class))).thenReturn(weekCards);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        List<WeekCard> foundWeekCards = weekCardService.findForLoggedUser();

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards).isEqualTo(weekCards);
        assertThat(foundWeekCards.size()).isEqualTo(2);
    }

    @Test
    public void findWeekCardsById_shouldReturnFoundWeekCard() {
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));

        WeekCard foundWeekCard = weekCardService.findById(weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard).isEqualTo(weekCard);
    }

    @Test
    public void updateWeekCardById_shouldReturnUpdatedWeekCardGoalId() {
        UpdateWeekCardDTO updateWeekCardDTO = UpdateWeekCardDTO.builder()
                .goalId(2L).description("new description")
                .weekStartDate(LocalDate.now().plusDays(1)).notes("new notes")
                .build();
        weekCard.setGoalId(2L);
        weekCard.setDescription("new description");
        weekCard.setWeekStartDate(LocalDate.now().plusDays(1));
        weekCard.setNotes("new notes");
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);

        WeekCard foundWeekCard = weekCardService.updateById(updateWeekCardDTO, weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard).isEqualTo(weekCard);
    }

    @Test
    public void updateWeekCardById_shouldReturnUpdatedWeekCardDescription() {
        UpdateWeekCardDTO updateWeekCardDTO = UpdateWeekCardDTO.builder()
                .goalId(2L).build();
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);

        WeekCard foundWeekCard = weekCardService.updateById(updateWeekCardDTO, weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard.getGoalId()).isEqualTo(2L);
    }

    @Test
    public void updateWeekCardById_shouldReturnUpdatedWeekCardWeekStartDate() {
        UpdateWeekCardDTO updateWeekCardDTO = UpdateWeekCardDTO.builder()
                .description("new description").build();
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);

        WeekCard foundWeekCard = weekCardService.updateById(updateWeekCardDTO, weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard.getDescription()).isEqualTo("new description");
    }

    @Test
    public void updateWeekCardById_shouldReturnUpdatedWeekCardNotes() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        UpdateWeekCardDTO updateWeekCardDTO = UpdateWeekCardDTO.builder()
                .weekStartDate(startDate).build();
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);

        WeekCard foundWeekCard = weekCardService.updateById(updateWeekCardDTO, weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard.getWeekStartDate()).isEqualTo(startDate);
        assertThat(foundWeekCard.getWeekEndDate()).isEqualTo(startDate.plusDays(7));
    }

    @Test
    public void removeTaskFromWeekCard_shouldRemoveTaskFromWeekCard() {
        weekCard.getWeekTasksIds().add(1L);
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));

        weekCardService.removeTask(1L, weekCard.getId());

        assertThat(weekCard).isNotNull();
        assertThat(weekCard.getWeekTasksIds().size()).isEqualTo(0);
    }

    @Test
    public void addTaskFromWeekCard_shouldAddTaskFromWeekCard() {
        when(weekCardRepository.save(Mockito.any(WeekCard.class))).thenReturn(weekCard);
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));

        weekCardService.addTask(1L, weekCard.getId());

        assertThat(weekCard).isNotNull();
        assertThat(weekCard.getWeekTasksIds().size()).isEqualTo(1);
    }

    @Test
    public void deleteWeekCardById_shouldReturnDeletedWeekCard() {
        when(weekCardRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(weekCard));

        WeekCard foundWeekCard = weekCardService.deleteById(weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
    }
}


