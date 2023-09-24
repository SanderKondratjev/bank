package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.AccountStatementService;
import com.homework.bank.service.BalanceService;
import com.homework.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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
    private AccountStatementService accountStatementService;

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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return createFailureModelAndView("Deposit amount cant be negative");
        }

        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {

            Transaction depositTransaction = transactionService.createDepositTransaction(account, amount, currency, description);

            balanceService.updateBalance(account, currency, amount);

            accountStatementService.createAccountStatement(account, depositTransaction, amount, currency, description);

            return createSuccessModelAndView("Deposit successful");
        } else {
            return createFailureModelAndView("Account not found");
        }
    }

    @PostMapping("/withdraw")
    public ModelAndView withdrawMoney(
            @RequestParam String accountName,
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam String currency
    ) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return createFailureModelAndView("Withdrawal amount cant be negative");
        }

        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {

            BigDecimal balance = balanceService.getCurrentBalance(account, currency);

            if (balance != null && balance.compareTo(amount) >= 0) {
                Transaction withdrawalTransaction = transactionService.createWithdrawalTransaction(account, amount, currency, description);

                balanceService.updateBalance(account, currency, amount.negate());

                accountStatementService.createAccountStatement(account, withdrawalTransaction, amount.negate(), currency, description);

                return createSuccessModelAndView("Withdrawal successful");
            } else {
                return createFailureModelAndView("Insufficient balance for withdrawal or currency not available");
            }
        } else {
            return createFailureModelAndView("Account not found");
        }
    }

    private ModelAndView createSuccessModelAndView(String message) {
        ModelAndView modelAndView = new ModelAndView("success");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    private ModelAndView createFailureModelAndView(String message) {
        ModelAndView modelAndView = new ModelAndView("failure");
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
