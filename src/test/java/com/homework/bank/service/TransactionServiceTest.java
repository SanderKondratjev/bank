package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateDepositTransaction() {
        Account account = new Account();
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "EUR";
        String description = "Deposit for testing";

        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any())).thenReturn(savedTransaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transaction depositTransaction = transactionService.createDepositTransaction(account, amount, currency, description);

        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction capturedTransaction = transactionCaptor.getValue();
        assertEquals("DEPOSIT", capturedTransaction.getTransactionType());
        assertEquals(amount, capturedTransaction.getAmount());
        assertEquals(currency, capturedTransaction.getCurrency());
        assertEquals(description, capturedTransaction.getDescription());
        assertEquals(account, capturedTransaction.getAccount());

        assertEquals(savedTransaction, depositTransaction);
    }

    @Test
    public void testCreateWithdrawalTransaction() {
        Account account = new Account();
        BigDecimal amount = new BigDecimal("50.00");
        String currency = "USD";
        String description = "Withdrawal for testing";

        Transaction savedTransaction = new Transaction();
        when(transactionRepository.save(any())).thenReturn(savedTransaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transaction withdrawalTransaction = transactionService.createWithdrawalTransaction(account, amount, currency, description);

        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction capturedTransaction = transactionCaptor.getValue();
        assertEquals("WITHDRAWAL", capturedTransaction.getTransactionType());
        assertEquals(amount.negate(), capturedTransaction.getAmount());
        assertEquals(currency, capturedTransaction.getCurrency());
        assertEquals(description, capturedTransaction.getDescription());
        assertEquals(account, capturedTransaction.getAccount());

        assertEquals(savedTransaction, withdrawalTransaction);
    }

    @Test
    public void testGetTransactionsForAccount() {
        Account account = new Account();
        account.setAccountId(1L);

        Transaction transaction1 = new Transaction();
        transaction1.setAccount(account);

        Transaction transaction2 = new Transaction();
        transaction2.setAccount(account);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        when(transactionRepository.findByAccount_AccountId(account.getAccountId())).thenReturn(transactions);

        List<Transaction> retrievedTransactions = transactionService.getTransactionsForAccount(account);

        verify(transactionRepository, times(1)).findByAccount_AccountId(account.getAccountId());

        assertEquals(2, retrievedTransactions.size());
        assertEquals(transactions, retrievedTransactions);
    }
}
