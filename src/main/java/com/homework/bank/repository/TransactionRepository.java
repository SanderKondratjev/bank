package com.homework.bank.repository;

import com.homework.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Define custom query methods if needed
}

