package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Transaction;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


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

}
