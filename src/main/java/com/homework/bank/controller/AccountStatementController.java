package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.AccountStatement;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.AccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/account-statement")
public class AccountStatementController {

    @Autowired
    private AccountStatementService accountStatementService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/search")
    public ModelAndView searchAccountStatement(@RequestParam String accountName) {
        Account account = accountService.getAccountByNameOrNumber(accountName);

        if (account != null) {
            List<AccountStatement> accountStatements = accountStatementService.getAccountStatementsForAccount(account);

            ModelAndView modelAndView = new ModelAndView("account-statement");
            modelAndView.addObject("accountStatements", accountStatements);

            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("failure");
            modelAndView.addObject("message", "Account not found");
            return modelAndView;
        }
    }
}
