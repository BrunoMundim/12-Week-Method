package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.dto.update.UpdateWeekCardDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.KeyResult;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.entity.WeekCard;
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

import static com.mundim.WeekMethod.entity.Goal.StatusType.NOT_STARTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeekCardControllerTest {

    private static User user;
    private static Goal goal;
    private static WeekCard weekCard;
    private static WeekCardDTO weekCardDTO;
    private static final Long userId = 1L;
    private static final Long goalId = 1L;
    private static final String userEmail = "email@email.com";
    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";

    @Mock
    private WeekCardService weekCardService;

    @InjectMocks
    private WeekCardController weekCardController;


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
                .status(NOT_STARTED).progressPercentage(0.0)
                .keyResults(Arrays.asList(keyResult1, keyResult2))
                .build();
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
    public void createWeekCardForLoggedUser_shouldReturnCreateWeekCard() {
        when(weekCardService.createForLoggedUser(weekCardDTO)).thenReturn(weekCard);

        ResponseEntity<WeekCard> createdWeekCard = weekCardController.createWeekCardForLoggedUser(weekCardDTO);

        assertThat(createdWeekCard).isNotNull();
        assertThat(createdWeekCard.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdWeekCard.getBody()).isEqualTo(weekCard);
    }

    @Test
    public void findWeekCardById_shouldReturnFoundWeekCard() {
        when(weekCardService.findById(weekCard.getId())).thenReturn(weekCard);

        ResponseEntity<WeekCard> foundWeekCard = weekCardController.findWeekCardById(weekCard.getId());

        assertThat(foundWeekCard).isNotNull();
        assertThat(foundWeekCard.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundWeekCard.getBody()).isEqualTo(weekCard);
    }

    @Test
    public void findWeekCardsForLoggedUser_shouldReturnFoundWeekCards() {
        List<WeekCard> weekCards = Arrays.asList(weekCard, weekCard);
        when(weekCardService.findForLoggedUser()).thenReturn(weekCards);

        ResponseEntity<List<WeekCard>> foundWeekCards = weekCardController.findWeekCardsForLoggedUser();

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundWeekCards.getBody()).isEqualTo(weekCards);
    }

    @Test
    public void updateWeekCardById_shouldReturnFoundWeekCards() {
        UpdateWeekCardDTO dto = UpdateWeekCardDTO.builder()
                .goalId(goal.getId()).description("new description").weekStartDate(LocalDate.now().plusDays(2))
                .notes("new notes").build();
        when(weekCardService.updateById(dto, weekCard.getId())).thenReturn(weekCard);

        ResponseEntity<WeekCard> foundWeekCards = weekCardController.updateWeekCardById(weekCard.getId(), dto);

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundWeekCards.getBody()).isEqualTo(weekCard);
    }

    @Test
    public void deleteWeekCardById_shouldReturnFoundWeekCards() {
        when(weekCardService.deleteById(weekCard.getId())).thenReturn(weekCard);

        ResponseEntity<WeekCard> foundWeekCards = weekCardController.deleteWeekCardById(weekCard.getId());

        assertThat(foundWeekCards).isNotNull();
        assertThat(foundWeekCards.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(foundWeekCards.getBody()).isEqualTo(weekCard);
    }

}
