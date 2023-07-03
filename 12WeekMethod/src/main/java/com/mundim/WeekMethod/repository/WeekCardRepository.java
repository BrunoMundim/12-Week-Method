package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.WeekCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekCardRepository extends JpaRepository<WeekCard, Long> {
}
