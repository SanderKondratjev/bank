package com.homework.bank.repository;

import com.homework.bank.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    // Define custom query methods if needed
}

