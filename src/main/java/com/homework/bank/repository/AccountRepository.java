package com.homework.bank.repository;

import com.homework.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);

    Account findByIban(String accountSearch);
}
