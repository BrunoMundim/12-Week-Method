package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.WeekCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekCardRepository extends JpaRepository<WeekCard, Long> {

    public List<WeekCard> findWeekCardsByGoalId(Long goalId);

}
