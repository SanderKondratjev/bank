package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/balances")
public class BalanceController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/check")
    public String showBalanceForm() {
        return "balances";
    }

    @PostMapping("/check")
    public ModelAndView checkBalance(@RequestParam String accountName) {
        Account account = accountService.getAccountByNameOrNumber(accountName);
        ModelAndView modelAndView = new ModelAndView("balances");

        if (account != null) {
            Balance balance = balanceService.getBalanceByAccount(account);
            if (balance != null) {
                modelAndView.addObject("message", "Balance: " + balance.getBalanceAmount() + " EUR");
            } else {
                modelAndView.addObject("message", "Balance not found for this account.");
            }
        } else {
            modelAndView.addObject("message", "Account not found.");
        }

        return modelAndView;
    }
}
