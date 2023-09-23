package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsForAccount(Account account) {
        return transactionRepository.findByAccount_AccountId(account.getAccountId());
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
