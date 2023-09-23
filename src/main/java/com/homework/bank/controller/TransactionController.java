package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.model.Transaction;
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
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }

    @PostMapping("/deposit")
    public ModelAndView depositMoney(
            @RequestParam String accountName,
            @RequestParam BigDecimal amount,
            @RequestParam String description
    ) {
        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {
            // Create a new transaction for the deposit
            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(account);
            depositTransaction.setTransactionType("DEPOSIT");
            depositTransaction.setAmount(amount);
            depositTransaction.setCurrency("EUR");
            depositTransaction.setDescription(description);
            depositTransaction.setTransactionDate(new Date());
            transactionService.createTransaction(depositTransaction);

            // Update the balance in the balance table
            Balance balance = balanceService.getBalanceByAccount(account);
            BigDecimal newBalance = balance.getBalanceAmount().add(amount);
            balance.setBalanceAmount(newBalance);
            balance.setBalanceDate(new Date());
            balanceService.updateBalance(balance);

            // Redirect to a success page or transaction history
            ModelAndView modelAndView = new ModelAndView("success");
            modelAndView.addObject("message", "Deposit successful");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }
}
