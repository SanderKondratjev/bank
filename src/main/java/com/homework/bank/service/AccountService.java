package com.homework.bank.service;

import com.homework.bank.controller.AccountController;
import com.homework.bank.model.Account;
import com.homework.bank.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountRepository accountRepository;

    public Account openAccount(String accountName) {
        String iban = generateIban();

        if (accountName.startsWith("accountName=")) {
            accountName = accountName.substring("accountName=".length());
        }

        Account account = new Account();
        account.setAccount_name(accountName);
        account.setIban(iban);

        return accountRepository.save(account);
    }

    private String generateIban() {
        StringBuilder accountNumber = new StringBuilder("EE");
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 18; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
