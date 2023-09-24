package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BalanceControllerTest {

    @InjectMocks
    private BalanceController balanceController;

    @Mock
    private AccountService accountService;

    @Mock
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckBalance() {
        String accountName = "account123";
        Account account = new Account();
        account.setAccountName(accountName);

        Balance balance = new Balance();
        balance.setBalanceAmount(BigDecimal.valueOf(100.00));

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getBalanceByAccount(account)).thenReturn(balance);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verify(balanceService).getBalanceByAccount(account);

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Balance: 100.0 EUR", modelAndView.getModel().get("message"));
    }

    @Test
    void testCheckBalanceAccountNotFound() {
        String accountName = "account123";

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(null);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verifyNoInteractions(balanceService);

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Account not found.", modelAndView.getModel().get("message"));
    }

    @Test
    void testCheckBalanceBalanceNotFound() {
        String accountName = "account123";
        Account account = new Account();
        account.setAccountName(accountName);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getBalanceByAccount(account)).thenReturn(null);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verify(balanceService).getBalanceByAccount(account);

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Balance not found for this account.", modelAndView.getModel().get("message"));
    }
}
