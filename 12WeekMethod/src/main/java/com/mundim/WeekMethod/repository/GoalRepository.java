package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    public List<Goal> findGoalsByUserId(Long userId);

}
