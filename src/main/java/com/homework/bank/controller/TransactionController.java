package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.model.Balance;
import com.homework.bank.model.Transaction;
import com.homework.bank.repository.AccountStatementRepository;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.BalanceService;
import com.homework.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private AccountStatementRepository accountStatementRepository;

    @PostMapping("/search")
    public ModelAndView searchAccount(@RequestParam String accountSearch) {
        Account account = accountService.getAccountByNameOrNumber(accountSearch);

        if (account != null) {
            List<Transaction> transactions = transactionService.getTransactionsForAccount(account);

            ModelAndView modelAndView = new ModelAndView("transactions");
            modelAndView.addObject("account", account);
            modelAndView.addObject("transactionList", transactions);

            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("failure");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }

    @PostMapping("/deposit")
    public ModelAndView depositMoney(
            @RequestParam String accountName,
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam String currency
    ) {
        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                ModelAndView modelAndView = new ModelAndView("failure");
                modelAndView.addObject("message", "Deposit amount cant be negative");
                return modelAndView;
            }
            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(account);
            depositTransaction.setTransactionType("DEPOSIT");
            depositTransaction.setAmount(amount);
            depositTransaction.setCurrency(currency);
            depositTransaction.setDescription(description);
            depositTransaction.setTransactionDate(new Date());
            transactionService.createTransaction(depositTransaction);

            Balance balance = balanceService.getBalanceByAccountAndCurrency(account, currency);
            if (balance == null) {
                balance = new Balance();
                balance.setAccount(account);
                balance.setBalanceAmount(BigDecimal.ZERO);
                balance.setCurrency(currency);
            }

            BigDecimal newBalance = balance.getBalanceAmount().add(amount);
            balance.setBalanceAmount(newBalance);
            balance.setBalanceDate(new Date());
            balanceService.updateBalance(balance);

            AccountStatement accountStatement = new AccountStatement();
            accountStatement.setAccount(account);
            accountStatement.setTransaction(depositTransaction);
            accountStatement.setTransactionDate(new Date());
            accountStatement.setAmount(amount);
            accountStatement.setCurrency(currency);
            accountStatement.setDescription(description);
            accountStatementRepository.save(accountStatement);

            ModelAndView modelAndView = new ModelAndView("success");
            modelAndView.addObject("message", "Deposit successful");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("failure");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }

    @PostMapping("/withdraw")
    public ModelAndView withdrawMoney(
            @RequestParam String accountName,
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam String currency
    ) {
        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                ModelAndView modelAndView = new ModelAndView("failure");
                modelAndView.addObject("message", "Withdrawal amount cant be negative");
                return modelAndView;
            }
            Balance balance = balanceService.getBalanceByAccountAndCurrency(account, currency);

            if (balance != null && balance.getBalanceAmount().compareTo(amount) >= 0) {
                Transaction withdrawalTransaction = new Transaction();
                withdrawalTransaction.setAccount(account);
                withdrawalTransaction.setTransactionType("WITHDRAWAL");
                withdrawalTransaction.setAmount(amount.negate());
                withdrawalTransaction.setCurrency(currency);
                withdrawalTransaction.setDescription(description);
                withdrawalTransaction.setTransactionDate(new Date());
                transactionService.createTransaction(withdrawalTransaction);

                BigDecimal newBalance = balance.getBalanceAmount().subtract(amount);
                balance.setBalanceAmount(newBalance);
                balance.setBalanceDate(new Date());
                balanceService.updateBalance(balance);

                AccountStatement accountStatement = new AccountStatement();
                accountStatement.setAccount(account);
                accountStatement.setTransaction(withdrawalTransaction);
                accountStatement.setTransactionDate(new Date());
                accountStatement.setAmount(amount.negate());
                accountStatement.setCurrency(currency);
                accountStatement.setDescription(description);
                accountStatementRepository.save(accountStatement);

                ModelAndView modelAndView = new ModelAndView("success");
                modelAndView.addObject("message", "Withdrawal successful");
                return modelAndView;
            } else {
                ModelAndView modelAndView = new ModelAndView("failure");
                modelAndView.addObject("message", "Insufficient balance for withdrawal or currency not available");
                return modelAndView;
            }
        } else {
            ModelAndView modelAndView = new ModelAndView("failure");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }


}
