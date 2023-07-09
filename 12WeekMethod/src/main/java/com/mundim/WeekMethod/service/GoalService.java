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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public GoalService(GoalRepository goalRepository, KeyResultRepository keyResultRepository, UserService userService, AuthenticationService authenticationService) {
        this.goalRepository = goalRepository;
        this.keyResultRepository = keyResultRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public Goal createGoalForLoggedUser(GoalDTO goalDTO) {
        User user = authenticationService.findUserByBearer();
        return goalRepository.save(new Goal(goalDTO, user.getId()));
    }

    public Goal findGoalById(Long goalId) {
        verifyUserAuthorizationForGoal(goalId);
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new BadRequestException("A goal with this id does not exists"));
    }

    public List<Goal> findGoalsFromLoggedUser() {
        User user = authenticationService.findUserByBearer();
        return goalRepository.findGoalByUserId(user.getId());
    }

    public List<Goal> findGoalsByUserId(Long userId) {
        verifyUserAuthorizationByUserId(userId);
        User user = userService.findUserById(userId);
        return goalRepository.findGoalByUserId(userId);
    }

    public Goal updateGoal(Long goalId, UpdateGoalDTO dto) {
        Goal goal = findGoalById(goalId);
        if (dto.title() != null) goal.setTitle(dto.title());
        if (dto.description() != null) goal.setDescription(dto.description());
        if (dto.keyResultsDescription() != null) {
            goal.setKeyResults(new ArrayList<>());
            for (int i = 0; i < dto.keyResultsDescription().size(); i++)
                goal.getKeyResults().add(new KeyResult(dto.keyResultsDescription().get(i), goalId));
        }
        return goalRepository.save(goal);
    }

    public Goal deleteGoalById(Long goalId) {
        Goal goal = findGoalById(goalId);
        goalRepository.deleteById(goalId);
        return goal;
    }

    public Goal inProgressGoal(Long goalId) {
        Goal goal = findGoalById(goalId);
        goal.setStatus(Goal.StatusType.IN_PROGRESS);
        goal.setStartDate(LocalDate.now());
        goal.setEndDate(LocalDate.now().plusMonths(3));
        return goalRepository.save(goal);
    }

    public Goal completeGoal(Long goalId) {
        Goal goal = findGoalById(goalId);
        goal.setStatus(Goal.StatusType.COMPLETED);
        goal.setCompletionDate(LocalDate.now());
        return goalRepository.save(goal);
    }

    public KeyResult findKeyResultById(Long keyResultId) {
       return keyResultRepository.findById(keyResultId)
                .orElseThrow(() -> new BadRequestException("Key Result not found"));
    }

    public Goal completeKeyResult(Long goalId, Long keyResultId) {
        Goal goal = findGoalById(goalId);
        changeCompletionStatusKeyResult(keyResultId, true);
        goal.setProgressPercentage(goal.calculateProgressPercentage());
        return goalRepository.save(goal);
    }

    public Goal uncompleteKeyResult(Long goalId, Long keyResultId) {
        Goal goal = findGoalById(goalId);
        changeCompletionStatusKeyResult(keyResultId, false);
        goal.setProgressPercentage(goal.calculateProgressPercentage());
        return goalRepository.save(goal);
    }

    private void changeCompletionStatusKeyResult(Long keyResultId, boolean status) {
        KeyResult keyResult = findKeyResultById(keyResultId);
        keyResult.setCompleted(status);
        keyResultRepository.save(keyResult);
    }

    private void verifyUserAuthorizationForGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new BadRequestException("A goal with this id does not exists"));
        User user = authenticationService.findUserByBearer();
        if (!goal.getUserId().equals(user.getId())
                && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            throw new UnauthorizedRequestException("Unauthorized action");
    }

    private void verifyUserAuthorizationByUserId(Long userId) {
        User user = authenticationService.findUserByBearer();
        if (!user.getId().equals(userId)
                && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedRequestException("Unauthorized action");
        }
    }

}
