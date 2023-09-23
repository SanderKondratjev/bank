package com.homework.bank.repository;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByAccount(Account account);

    Balance findByAccountAndCurrency(Account account, String currency);
}
