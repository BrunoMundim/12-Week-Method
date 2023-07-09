package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
