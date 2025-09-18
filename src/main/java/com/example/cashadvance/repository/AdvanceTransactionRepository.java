package com.example.cashadvance.repository;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvanceTransactionRepository extends JpaRepository<AdvanceTransaction, Long> {
    List<AdvanceTransaction> findByUserOrderByTimestampDesc(User user);
}
