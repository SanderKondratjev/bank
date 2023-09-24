package com.homework.bank.integration;

import com.homework.bank.controller.AccountStatementController;
import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.AccountStatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(AccountStatementController.class)
@AutoConfigureMockMvc
public class AccountStatementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountStatementService accountStatementService;

    @BeforeEach
    public void setUp() {
        // Mock some data for the test
        Account testAccount = new Account();
        testAccount.setAccountId(1L);
        testAccount.setAccountName("TestAccount");

        List<AccountStatement> accountStatements = new ArrayList<>();
        AccountStatement statement1 = new AccountStatement();
        statement1.setStatementId(1L);
        statement1.setAccount(testAccount);
        statement1.setDescription("Transaction 1");
        accountStatements.add(statement1);

        when(accountService.getAccountByNameOrNumber("TestAccount")).thenReturn(testAccount);
        when(accountStatementService.getAccountStatementsForAccount(testAccount)).thenReturn(accountStatements);
    }

    @Test
    public void testSearchAccountStatementIntegration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/account-statement/search")
                        .param("accountName", "TestAccount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("account-statement"))
                .andExpect(model().attributeExists("accountStatements"))
                .andExpect(model().attribute("accountStatements", hasSize(1)))
                .andExpect(model().attribute("accountStatements", hasItem(
                        allOf(
                                hasProperty("statementId", is(1L)),
                                hasProperty("description", is("Transaction 1"))
                        )
                )));
    }
}
