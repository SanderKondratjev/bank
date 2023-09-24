package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateBalance() {
        Account account = new Account();
        account.setIban("1234567890");
        String currency = "USD";
        BigDecimal amount = new BigDecimal("100.00");

        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setCurrency(currency);
        balance.setBalanceAmount(new BigDecimal("500.00"));

        when(balanceRepository.findTop1ByAccountAndCurrencyOrderByBalanceDateDesc(account, currency))
                .thenReturn(balance);

        balanceService.updateBalance(account, currency, amount);

        verify(balanceRepository).save(argThat(newBalance -> {
            assertEquals(account, newBalance.getAccount());
            assertEquals(currency, newBalance.getCurrency());
            assertEquals(new BigDecimal("600.00"), newBalance.getBalanceAmount());
            return true;
        }));
    }

    @Test
    void testGetTransactionType() {
        BigDecimal positiveAmount = new BigDecimal("100.00");
        assertEquals("Deposit", balanceService.getTransactionType(positiveAmount));

        BigDecimal negativeAmount = new BigDecimal("-50.00");
        assertEquals("Withdraw", balanceService.getTransactionType(negativeAmount));

        BigDecimal zeroAmount = BigDecimal.ZERO;
        assertEquals("No Transaction", balanceService.getTransactionType(zeroAmount));
    }
}
