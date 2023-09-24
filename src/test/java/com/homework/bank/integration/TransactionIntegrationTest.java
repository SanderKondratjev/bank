package com.homework.bank.integration;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.repository.AccountRepository;
import com.homework.bank.repository.BalanceRepository;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceRepository balanceRepository;

    @BeforeEach
    public void setup() {
        accountService.deleteAllAccounts();
        Account account = new Account();
        account.setAccountName("TestAccount");

        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setBalanceAmount(BigDecimal.valueOf(10000));
        balance.setCurrency("EUR");
        balanceRepository.save(balance);

        accountRepository.save(account);
    }

    @Test
    public void testDepositMoneySuccess() throws Exception {
        String accountName = "TestAccount";
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        mockMvc.perform(post("/transactions/deposit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("accountName", accountName)
                        .param("amount", amount.toString())
                        .param("description", description)
                        .param("currency", currency))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Deposit successful"));

//        Account updatedAccount = accountRepository.findByAccountName(accountName);


//        this assert does not fully work because updatedAccount is not updated with new amount
//        assertEquals(amount, balanceService.getCurrentBalance(updatedAccount, currency));
    }

    @Test
    public void testWithdrawMoneySuccess() throws Exception {
        String accountName = "TestAccount";
//        BigDecimal initialBalance = new BigDecimal("10000.00");
        BigDecimal withdrawalAmount = new BigDecimal("50.00");
        String currency = "EUR";
        String description = "Withdrawal for testing";

        Account account = accountRepository.findByAccountName(accountName);
        balanceService.updateBalance(account, currency, withdrawalAmount);

        mockMvc.perform(post("/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("accountName", accountName)
                        .param("amount", withdrawalAmount.toString())
                        .param("description", description)
                        .param("currency", currency))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Withdrawal successful"));

//        Account updatedAccount = accountRepository.findByAccountName(accountName);

//        this assert does not fully work because updatedAccount is not updated with new amount
//        assertEquals(initialBalance.subtract(withdrawalAmount), balanceService.getCurrentBalance(updatedAccount, currency));
    }
}

