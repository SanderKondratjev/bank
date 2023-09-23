package com.homework.bank.repository;

import com.homework.bank.model.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatementRepository extends JpaRepository<AccountStatement, Long> {
    // Define custom query methods if needed
}

