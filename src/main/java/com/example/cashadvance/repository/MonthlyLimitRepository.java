package com.example.cashadvance.repository;

import com.example.cashadvance.model.MonthlyLimit;
import com.example.cashadvance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyLimitRepository extends JpaRepository<MonthlyLimit, Long> {
    Optional<MonthlyLimit> findByUserAndMonthYear(User user, String monthYear);
}
