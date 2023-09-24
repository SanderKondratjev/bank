package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.AccountStatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountStatementControllerTest {

    @Mock
    private AccountStatementService accountStatementService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountStatementController accountStatementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchAccountStatementSuccess() {
        String accountName = "account123";

        Account account = new Account();
        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);

        List<AccountStatement> accountStatements = new ArrayList<>();
        when(accountStatementService.getAccountStatementsForAccount(account)).thenReturn(accountStatements);

        ModelAndView modelAndView = accountStatementController.searchAccountStatement(accountName);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);
        verify(accountStatementService, times(1)).getAccountStatementsForAccount(account);

        assertEquals("account-statement", modelAndView.getViewName());
        assertEquals(accountStatements, modelAndView.getModel().get("accountStatements"));
    }

    @Test
    public void testSearchAccountStatementFailureAccountNotFound() {
        String accountName = "nonExistentAccount";

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(null);

        ModelAndView modelAndView = accountStatementController.searchAccountStatement(accountName);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);
        verify(accountStatementService, never()).getAccountStatementsForAccount(any(Account.class));

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Account not found", modelAndView.getModel().get("message"));
    }
}
