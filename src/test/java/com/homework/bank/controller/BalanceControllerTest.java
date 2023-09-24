package com.homework.bank.controller;

import com.homework.bank.model.Account;
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
    void testCheckBalanceWithBalanceFound() {
        String accountName = "account123";
        Account account = new Account();
        account.setAccountName(accountName);

        BigDecimal balanceAmount = BigDecimal.valueOf(100.00);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getCurrentBalance(account, "EUR")).thenReturn(balanceAmount);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verify(balanceService).getCurrentBalance(account, "EUR");

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Balances: 100.0 EUR", modelAndView.getModel().get("message"));
    }

    @Test
    void testCheckBalanceWithAccountNotFound() {
        String accountName = "account123";
        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(null);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verifyNoInteractions(balanceService);

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Account not found.", modelAndView.getModel().get("message"));
    }

    @Test
    void testCheckBalanceWithBalanceNotFound() {
        String accountName = "account123";
        Account account = new Account();
        account.setAccountName(accountName);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getCurrentBalance(account, "EUR")).thenReturn(null);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verify(balanceService).getCurrentBalance(account, "EUR");

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Balances not found for this account.", modelAndView.getModel().get("message"));
    }

    @Test
    void testCheckBalanceWithMultipleCurrencies() {
        String accountName = "account123";
        Account account = new Account();
        account.setAccountName(accountName);

        BigDecimal balanceEur = BigDecimal.valueOf(100.00);
        BigDecimal balanceUsd = BigDecimal.valueOf(200.00);
        BigDecimal balanceGbp = BigDecimal.valueOf(300.00);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getCurrentBalance(account, "EUR")).thenReturn(balanceEur);
        when(balanceService.getCurrentBalance(account, "USD")).thenReturn(balanceUsd);
        when(balanceService.getCurrentBalance(account, "GBP")).thenReturn(balanceGbp);

        ModelAndView modelAndView = balanceController.checkBalance(accountName);

        verify(accountService).getAccountByNameOrNumber(accountName);
        verify(balanceService).getCurrentBalance(account, "EUR");
        verify(balanceService).getCurrentBalance(account, "USD");
        verify(balanceService).getCurrentBalance(account, "GBP");

        assertEquals("balances", modelAndView.getViewName());
        assertEquals("Balances: 100.0 EUR, 200.0 USD, 300.0 GBP", modelAndView.getModel().get("message"));
    }
}

