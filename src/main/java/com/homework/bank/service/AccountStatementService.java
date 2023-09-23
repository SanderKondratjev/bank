package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.repository.AccountStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountStatementService {

    @Autowired
    private AccountStatementRepository accountStatementRepository;

    public List<AccountStatement> getAccountStatementsForAccount(Account account) {
        return accountStatementRepository.findAccountStatementsByAccount_AccountId(account.getAccountId());
    }
}
