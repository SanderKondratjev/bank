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

import javax.persistence.NonUniqueResultException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;


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
        if (accountName.startsWith("accountName=")) {
            accountName = accountName.substring("accountName=".length());
        }

        try {
            Account existingAccount = accountRepository.findByAccountName(accountName);
            if (existingAccount != null) {
                log.error("An account with the name {} already exists.", accountName);
                return null;
            }

            String iban = generateIban();

            Account account = new Account();
            account.setAccountName(accountName);
            account.setIban(iban);

            account = accountRepository.save(account);

            Balance initialBalance = new Balance();
            initialBalance.setAccount(account);
            initialBalance.setBalanceAmount(BigDecimal.ZERO);
            initialBalance.setCurrency("EUR");
            initialBalance.setBalanceDate(new Date());
            balanceRepository.save(initialBalance);

            Transaction initialTransaction = new Transaction();
            initialTransaction.setAccount(account);
            initialTransaction.setTransactionType("OPENING");
            initialTransaction.setAmount(BigDecimal.ZERO);
            initialTransaction.setCurrency("EUR");
            initialTransaction.setDescription("Account opened");
            initialTransaction.setTransactionDate(new Date());
            transactionRepository.save(initialTransaction);

            AccountStatement initialStatement = new AccountStatement();
            initialStatement.setAccount(account);
            initialStatement.setTransaction(initialTransaction);
            initialStatement.setTransactionDate(new Date());
            initialStatement.setAmount(BigDecimal.ZERO);
            initialStatement.setCurrency("EUR");
            initialStatement.setDescription("Account opened");
            accountStatementRepository.save(initialStatement);

            return account;
        } catch (
                NonUniqueResultException e) {
            log.error("This account name has already take: " + accountName);
            return null;
        }
    }

    public static String generateIban() {
        String country = "EE";

        StringBuilder accountNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            accountNumber.append(random.nextInt(10));
        }

        String iban = country + "00" + accountNumber;
        int checksum = calculateIbanChecksum(iban);

        String ibanChecksum = String.format("%02d", checksum);
        iban = country + ibanChecksum + "0000" + accountNumber;

        return iban;
    }

    public static int calculateIbanChecksum(String iban) {
        iban = iban.substring(4) + iban.substring(0, 2) + "00";

        String digits = iban.replaceAll("[^0-9]", "");

        BigInteger ibanNumber = new BigInteger(digits);

        return 98 - ibanNumber.mod(BigInteger.valueOf(97)).intValue();
    }

    public Account getAccountByNameOrNumber(String accountSearch) {
        Account account = accountRepository.findByAccountName(accountSearch);

        if (account == null) {
            account = accountRepository.findByIban(accountSearch);
        }

        return account;
    }
}
