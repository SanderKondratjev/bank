package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createDepositTransaction(Account account, BigDecimal amount, String currency, String description) {
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(account);
        depositTransaction.setTransactionType("DEPOSIT");
        depositTransaction.setAmount(amount);
        depositTransaction.setCurrency(currency);
        depositTransaction.setDescription(description);
        depositTransaction.setTransactionDate(new Date());
        return transactionRepository.save(depositTransaction);
    }

    public Transaction createWithdrawalTransaction(Account account, BigDecimal amount, String currency, String description) {
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccount(account);
        withdrawalTransaction.setTransactionType("WITHDRAWAL");
        withdrawalTransaction.setAmount(amount.negate());
        withdrawalTransaction.setCurrency(currency);
        withdrawalTransaction.setDescription(description);
        withdrawalTransaction.setTransactionDate(new Date());
        return transactionRepository.save(withdrawalTransaction);
    }


    public List<Transaction> getTransactionsForAccount(Account account) {
        return transactionRepository.findByAccount_AccountId(account.getAccountId());
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
