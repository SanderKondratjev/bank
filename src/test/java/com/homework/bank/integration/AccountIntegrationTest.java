package com.homework.bank.integration;

import com.homework.bank.model.Account;
import com.homework.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService.deleteAllAccounts();
    }

    @Test
    public void testOpenAccountSuccess() throws Exception {
        String accountName = "TestAccount";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountName))
                .andExpect(status().isOk())
                .andExpect(view().name("account-details"))
                .andExpect(model().attributeExists("accountName", "accountNumber"))
                .andReturn();

        Account account = accountService.getAccountByNameOrNumber(accountName);

        assertEquals("TestAccount", result.getModelAndView().getModel().get("accountName"));
        assertEquals(account.getIban(), result.getModelAndView().getModel().get("accountNumber"));

        assertEquals(accountName, account.getAccountName());
    }

    @Test
    public void testOpenAccountFailure() throws Exception {
        Account existingAccount = new Account();
        existingAccount.setAccountName("TestAccount");
        existingAccount.setIban("EE123456789012345678");
        accountService.openAccount(existingAccount.getAccountName());

        String accountName = "TestAccount";

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountName))
                .andExpect(status().isOk())
                .andExpect(view().name("failure"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Account creation failed. Account name already taken. Try again with new account name."));
    }
}
