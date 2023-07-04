package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
