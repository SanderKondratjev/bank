package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.AccountStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountStatementService {

    @Autowired
    private AccountStatementRepository accountStatementRepository;

    public void createAccountStatement(Account account, Transaction depositTransaction, BigDecimal amount, String currency, String description) {
        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setAccount(account);
        accountStatement.setTransaction(depositTransaction);
        accountStatement.setTransactionDate(new Date());
        accountStatement.setAmount(amount);
        accountStatement.setCurrency(currency);
        accountStatement.setDescription(description);
        accountStatementRepository.save(accountStatement);
    }

    public List<AccountStatement> getAccountStatementsForAccount(Account account) {
        return accountStatementRepository.findAccountStatementsByAccount_AccountId(account.getAccountId());
    }
}
