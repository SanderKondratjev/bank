package com.homework.bank.service;

import com.homework.bank.controller.AccountController;
import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.model.Balance;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.AccountRepository;
import com.homework.bank.repository.AccountStatementRepository;
import com.homework.bank.repository.BalanceRepository;
import com.homework.bank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;


@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountStatementRepository accountStatementRepository;

    public Account openAccount(String accountName) {
        String iban = generateIban();

        if (accountName.startsWith("accountName=")) {
            accountName = accountName.substring("accountName=".length());
        }

        // Create a new account
        Account account = new Account();
        account.setAccount_name(accountName);
        account.setIban(iban);

        // Save the account to the database first
        account = accountRepository.save(account);

        // Set initial balance to 0 with the current date and time
        Balance initialBalance = new Balance();
        initialBalance.setAccount(account);
        initialBalance.setBalance_amount(BigDecimal.ZERO);
        initialBalance.setBalance_date(new Date()); // Set the balance_date to the current date and time
        balanceRepository.save(initialBalance);

        // Create an initial transaction for account opening (if needed)
        Transaction initialTransaction = new Transaction();
        initialTransaction.setAccount(account);
        initialTransaction.setTransaction_type("OPENING");
        initialTransaction.setAmount(BigDecimal.ZERO);
        initialTransaction.setCurrency("EUR");
        initialTransaction.setDescription("Account opened");

// Set the transaction_date to the current date and time
        initialTransaction.setTransaction_date(new Date());

        transactionRepository.save(initialTransaction);

        // Create an initial account statement (if needed)
        AccountStatement initialStatement = new AccountStatement();
        initialStatement.setAccount(account);
        initialStatement.setTransaction(initialTransaction);
        initialStatement.setTransaction_date(new Date());
        initialStatement.setAmount(BigDecimal.ZERO);
        initialStatement.setCurrency("EUR");
        initialStatement.setDescription("Account opened");
        accountStatementRepository.save(initialStatement);

        return account;
    }


    private String generateIban() { // TODO The system should generate a valid IBAN for the account (ISO 13616) during the account creation process
        StringBuilder accountNumber = new StringBuilder("EE");
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 18; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
