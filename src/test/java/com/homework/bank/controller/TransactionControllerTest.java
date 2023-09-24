package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.AccountStatementService;
import com.homework.bank.service.BalanceService;
import com.homework.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private BalanceService balanceService;

    @Mock
    private AccountStatementService accountStatementService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchAccountFound() {
        String accountSearch = "account123";
        Account account = new Account();
        account.setAccountName(accountSearch);
        List<Transaction> transactions = new ArrayList<>();
        when(accountService.getAccountByNameOrNumber(accountSearch)).thenReturn(account);
        when(transactionService.getTransactionsForAccount(account)).thenReturn(transactions);

        ModelAndView modelAndView = transactionController.searchAccount(accountSearch);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountSearch);
        verify(transactionService, times(1)).getTransactionsForAccount(account);
        assertEquals("transactions", modelAndView.getViewName());
        assertEquals(account, modelAndView.getModel().get("account"));
        assertEquals(transactions, modelAndView.getModel().get("transactionList"));
    }

    @Test
    public void testSearchAccountNotFound() {
        String accountSearch = "account123";
        when(accountService.getAccountByNameOrNumber(accountSearch)).thenReturn(null);

        ModelAndView modelAndView = transactionController.searchAccount(accountSearch);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountSearch);
        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Account not found", modelAndView.getModel().get("message"));
    }

    @Test
    public void testDepositMoneySuccess() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        Account account = new Account();
        account.setAccountName(accountName);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(transactionService.createDepositTransaction(account, amount, currency, description)).thenReturn(new Transaction());

        doNothing().when(accountStatementService)
                .createAccountStatement(any(), any(), any(), any(), any());

        ModelAndView modelAndView = transactionController.depositMoney(accountName, amount, description, currency);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);
        verify(transactionService, times(1)).createDepositTransaction(account, amount, currency, description);
        verify(balanceService, times(1)).updateBalance(account, currency, amount);

        assertEquals("success", modelAndView.getViewName());
        assertEquals("Deposit successful", modelAndView.getModel().get("message"));
    }

    @Test
    public void testWithdrawMoneySuccess() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("50.00");
        String currency = "USD";
        String description = "Withdrawal for testing";

        Account account = new Account();
        account.setAccountName(accountName);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getCurrentBalance(account, currency)).thenReturn(new BigDecimal("100.00"));
        when(transactionService.createWithdrawalTransaction(account, amount, currency, description)).thenReturn(new Transaction());

        doNothing().when(accountStatementService)
                .createAccountStatement(any(), any(), any(), any(), any());

        ModelAndView modelAndView = transactionController.withdrawMoney(accountName, amount, description, currency);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);
        verify(balanceService, times(1)).getCurrentBalance(account, currency);
        verify(transactionService, times(1)).createWithdrawalTransaction(account, amount, currency, description);
        verify(balanceService, times(1)).updateBalance(account, currency, amount.negate());

        assertEquals("success", modelAndView.getViewName());
        assertEquals("Withdrawal successful", modelAndView.getModel().get("message"));
    }

    @Test
    public void testWithdrawMoneyFailureInsufficientBalance() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("200.00");
        String currency = "USD";
        String description = "Withdrawal for testing";

        Account account = new Account();
        account.setAccountName(accountName);

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(account);
        when(balanceService.getCurrentBalance(account, currency)).thenReturn(new BigDecimal("100.00"));

        ModelAndView modelAndView = transactionController.withdrawMoney(accountName, amount, description, currency);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);
        verify(balanceService, times(1)).getCurrentBalance(account, currency);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Insufficient balance for withdrawal or currency not available", modelAndView.getModel().get("message"));
    }

    @Test
    public void testWithdrawMoneyFailureAccountNotFound() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("50.00");
        String currency = "USD";
        String description = "Withdrawal for testing";

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(null);

        ModelAndView modelAndView = transactionController.withdrawMoney(accountName, amount, description, currency);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Account not found", modelAndView.getModel().get("message"));
    }

    @Test
    public void testWithdrawMoneyFailureNegativeAmount() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("-50.00");
        String currency = "USD";
        String description = "Withdrawal for testing";

        ModelAndView modelAndView = transactionController.withdrawMoney(accountName, amount, description, currency);

        verify(accountService, never()).getAccountByNameOrNumber(accountName);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Withdrawal amount cant be negative", modelAndView.getModel().get("message"));
    }

    @Test
    public void testDepositMoneyFailureAccountNotFound() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        when(accountService.getAccountByNameOrNumber(accountName)).thenReturn(null);

        ModelAndView modelAndView = transactionController.depositMoney(accountName, amount, description, currency);

        verify(accountService, times(1)).getAccountByNameOrNumber(accountName);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Account not found", modelAndView.getModel().get("message"));
    }

    @Test
    public void testDepositMoneyFailureNegativeAmount() {
        String accountName = "account123";
        BigDecimal amount = new BigDecimal("-100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        ModelAndView modelAndView = transactionController.depositMoney(accountName, amount, description, currency);

        verify(accountService, never()).getAccountByNameOrNumber(accountName);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Deposit amount cant be negative", modelAndView.getModel().get("message"));
    }
}
