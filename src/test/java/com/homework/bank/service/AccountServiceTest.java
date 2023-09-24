package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.repository.AccountRepository;
import com.homework.bank.repository.BalanceRepository;
import com.homework.bank.repository.TransactionRepository;
import com.homework.bank.repository.AccountStatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountStatementRepository accountStatementRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOpenAccount() {
        String accountName = "TestAccount";

        when(accountRepository.findByAccountName(accountName)).thenReturn(null);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account savedAccount = invocation.getArgument(0);
            savedAccount.setAccountId(1L);
            return savedAccount;
        });

        Account account = accountService.openAccount(accountName);

        assertNotNull(account);
        assertEquals(accountName, account.getAccountName());

        verify(balanceRepository, times(1)).save(any());
        verify(transactionRepository, times(1)).save(any());
        verify(accountStatementRepository, times(1)).save(any());
    }

    @Test
    public void testOpenAccountExistingAccount() {
        String accountName = "TestAccount";

        when(accountRepository.findByAccountName(accountName)).thenReturn(new Account());

        Account account = accountService.openAccount(accountName);

        assertNull(account);
    }

    @Test
    public void testGetAccountByNameOrNumberByName() {
        String accountName = "TestAccount";
        Account expectedAccount = new Account();

        when(accountRepository.findByAccountName(accountName)).thenReturn(expectedAccount);

        Account account = accountService.getAccountByNameOrNumber(accountName);

        assertEquals(expectedAccount, account);
    }

    @Test
    public void testGetAccountByNameOrNumberByIban() {
        String iban = "EE12345678901234567890";
        Account expectedAccount = new Account();

        when(accountRepository.findByIban(iban)).thenReturn(expectedAccount);

        Account account = accountService.getAccountByNameOrNumber(iban);

        assertEquals(expectedAccount, account);
    }
}
