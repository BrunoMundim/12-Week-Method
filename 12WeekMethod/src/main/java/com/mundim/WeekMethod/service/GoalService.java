package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.GoalDTO;
import com.mundim.WeekMethod.entity.Goal;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    public Goal createGoal(GoalDTO goalDTO){
        return goalRepository.save(new Goal(goalDTO));
    }

    public Goal findGoalById(Long goalId){
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new BadRequestException("A goal with this id does not exists"));
    }

    public Goal updateGoal(Long goalId, GoalDTO goalDTO){
        Goal goal = findGoalById(goalId);
        if(goalDTO.title() != null) goal.setTitle(goalDTO.title());
        if(goalDTO.description() != null) goal.setDescription(goalDTO.description());
        if(goalDTO.startDate() != null) goal.setStartDate(goalDTO.startDate());
        if(goalDTO.endDate() != null) goal.setEndDate(goalDTO.endDate());
        if(goalDTO.status() != null) goal.setStatus(goalDTO.status());
        return goalRepository.save(goal);
    }

    public Goal deleteGoalById(Long goalId){
        Goal goal = findGoalById(goalId);
        goalRepository.deleteById(goalId);
        return goal;
    }

    public Goal addKeyResult(Long goalId, Goal.KeyResult keyResult){
        Goal goal = findGoalById(goalId);
        goal.getKeyResults().add(keyResult);
        return goalRepository.save(goal);
    }

}
