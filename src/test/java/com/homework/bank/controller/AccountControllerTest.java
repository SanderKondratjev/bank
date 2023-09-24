package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOpenAccountSuccess() {
        String accountName = "testAccount";
        Account mockAccount = new Account();
        mockAccount.setAccountName(accountName);
        mockAccount.setIban("mockedIBAN123");

        when(accountService.openAccount(accountName)).thenReturn(mockAccount);

        ModelAndView modelAndView = accountController.openAccount(accountName);

        assertEquals("account-details", modelAndView.getViewName());
        assertEquals("testAccount", modelAndView.getModel().get("accountName"));
        assertEquals("mockedIBAN123", modelAndView.getModel().get("accountNumber"));
    }

    @Test
    public void testOpenAccountFailure() {
        String accountName = "existingAccount";

        when(accountService.openAccount(accountName)).thenReturn(null);

        ModelAndView modelAndView = accountController.openAccount(accountName);

        assertEquals("failure", modelAndView.getViewName());
        assertEquals("Account creation failed. Account name already taken. Try again with new account name.", modelAndView.getModel().get("message"));
    }
}
