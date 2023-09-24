package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.AccountStatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountStatementServiceTest {

    @Mock
    private AccountStatementRepository accountStatementRepository;

    @InjectMocks
    private AccountStatementService accountStatementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountStatement() {
        Account account = new Account();
        Transaction depositTransaction = new Transaction();
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        AccountStatement savedAccountStatement = new AccountStatement();
        when(accountStatementRepository.save(any())).thenReturn(savedAccountStatement);

        accountStatementService.createAccountStatement(account, depositTransaction, amount, currency, description);

        verify(accountStatementRepository, times(1)).save(any(AccountStatement.class));

        ArgumentCaptor<AccountStatement> accountStatementCaptor = ArgumentCaptor.forClass(AccountStatement.class);
        verify(accountStatementRepository).save(accountStatementCaptor.capture());

        AccountStatement capturedAccountStatement = accountStatementCaptor.getValue();
        assertEquals(account, capturedAccountStatement.getAccount());
        assertEquals(depositTransaction, capturedAccountStatement.getTransaction());
        assertEquals(amount, capturedAccountStatement.getAmount());
        assertEquals(currency, capturedAccountStatement.getCurrency());
        assertEquals(description, capturedAccountStatement.getDescription());
    }

    @Test
    public void testGetAccountStatementsForAccount() {
        Account account = new Account();
        account.setAccountId(1L);

        AccountStatement statement1 = new AccountStatement();
        statement1.setAccount(account);

        AccountStatement statement2 = new AccountStatement();
        statement2.setAccount(account);

        List<AccountStatement> accountStatements = new ArrayList<>();
        accountStatements.add(statement1);
        accountStatements.add(statement2);

        when(accountStatementRepository.findAccountStatementsByAccount_AccountId(account.getAccountId())).thenReturn(accountStatements);

        List<AccountStatement> retrievedAccountStatements = accountStatementService.getAccountStatementsForAccount(account);

        verify(accountStatementRepository, times(1)).findAccountStatementsByAccount_AccountId(account.getAccountId());

        assertEquals(accountStatements.size(), retrievedAccountStatements.size());
        assertEquals(accountStatements, retrievedAccountStatements);
    }
}
