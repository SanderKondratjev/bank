package com.homework.bank.repository;

import com.homework.bank.model.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatementRepository extends JpaRepository<AccountStatement, Long> {
    List<AccountStatement> findAccountStatementsByAccount_AccountId(Long accountId);
}
