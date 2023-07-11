package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.dto.update.UpdateWeekCardDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.WeekCardRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.NULL_FIELD;
import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.WEEK_CARD_NOT_FOUND_BY_ID;

@Service
public class WeekCardService {

    private final AuthenticationService authenticationService;
    private final WeekCardRepository weekCardRepository;
    private final GoalService goalService;
    private final TaskService taskService;

    @Autowired
    public WeekCardService(
            WeekCardRepository weekCardRepository,
            AuthenticationService authenticationService,
            GoalService goalService,
            @Lazy TaskService taskService
    ) {
        this.weekCardRepository = weekCardRepository;
        this.authenticationService = authenticationService;
        this.goalService = goalService;
        this.taskService = taskService;
    }

    public WeekCard createWeekCardForLoggedUser(WeekCardDTO weekCardDTO) {
        goalService.verifyUserAuthorizationForGoal(weekCardDTO.goalId());
        verifyDtoNullFields(weekCardDTO);
        return weekCardRepository.save(new WeekCard(weekCardDTO));
    }

    public List<WeekCard> findWeekCardsByGoalId(Long goalId) {
        Goal goal = goalService.findById(goalId); // Verify authorization
        return weekCardRepository.findWeekCardsByGoalId(goalId);
    }

    public List<WeekCard> findWeekCardsForLoggedUser() {
        Long userId = authenticationService.findUserByBearer().getId();
        List<Goal> goals = goalService.findByUserId(userId);
        List<WeekCard> weekCards = new ArrayList<>();
        for(Goal goal:goals)
            weekCards.addAll(findWeekCardsByGoalId(goal.getId()));
        return weekCards;
    }

    public List<WeekCard> findWeekCardsByUserId(Long userId) {
        List<Goal> goals = goalService.findByUserId(userId);
        List<WeekCard> weekCards = new ArrayList<>();
        for(Goal goal:goals)
            weekCards.addAll(findWeekCardsByGoalId(goal.getId()));
        return weekCards;
    }

    public WeekCard findWeekCardById(Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        return findWeekCard(weekCardId);
    }

    public WeekCard updateWeekCardById(UpdateWeekCardDTO dto, Long weekCardId) {
        WeekCard weekCard = findWeekCardById(weekCardId);
        verifyUserAuthorizationForWeekCard(weekCardId);

        if (dto.goalId() != null) {
            goalService.verifyUserAuthorizationForGoal(dto.goalId());
            weekCard.setGoalId(dto.goalId());
        }
        if (dto.description() != null) weekCard.setDescription(dto.description());
        if (dto.weekStartDate() != null) {
            weekCard.setWeekStartDate(dto.weekStartDate());
            weekCard.setWeekEndDate(weekCard.getWeekStartDate().plusDays(7));
        }
        if (dto.notes() != null) weekCard.setNotes(dto.notes());

        return weekCardRepository.save(weekCard);
    }

    public void removeTaskFromWeekCard(Long taskId, Long weekCardId) {
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().remove(taskId);
        weekCardRepository.save(weekCard);
    }

    public void addTaskToWeekCard(Long taskId, Long weekCardId) {
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().add(taskId);
        weekCardRepository.save(weekCard);
    }

    public WeekCard deleteWeekCardById(Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        WeekCard weekCard = findWeekCardById(weekCardId);
        for (Long taskId : weekCard.getWeekTasksIds()) {
            taskService.deleteTaskById(taskId);
        }
        weekCardRepository.deleteById(weekCardId);
        return weekCard;
    }

    public void deleteAllWeekCardByUserId(Long userId) {
        List<WeekCard> weekCards = findWeekCardsByUserId(userId);
        for (WeekCard weekCard : weekCards) {
            for (Long taskId : weekCard.getWeekTasksIds()) {
                taskService.deleteTaskById(taskId);
            }
            weekCardRepository.delete(weekCard);
        }
    }

    public void verifyUserAuthorizationForWeekCard(Long weekCardId) {
        WeekCard weekCard = findWeekCard(weekCardId);
        goalService.verifyUserAuthorizationForGoal(weekCard.getGoalId());
    }

    private WeekCard findWeekCard(Long weekCardId) {
        return weekCardRepository.findById(weekCardId)
                .orElseThrow(() -> new BadRequestException(
                        WEEK_CARD_NOT_FOUND_BY_ID.params(weekCardId.toString()).getMessage())
                );
    }

    private void verifyDtoNullFields(WeekCardDTO dto) {
        if (dto.description() == null) throw new NullFieldException(NULL_FIELD.params("'description'").getMessage());
        if (dto.notes() == null) throw new NullFieldException(NULL_FIELD.params("'notes'").getMessage());
        if (dto.weekStartDate() == null)
            throw new NullFieldException(NULL_FIELD.params("'weekStartDate'").getMessage());
    }

}
