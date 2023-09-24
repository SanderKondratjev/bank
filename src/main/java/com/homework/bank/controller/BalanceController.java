package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.service.AccountService;
import com.homework.bank.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

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
            List<String> currencies = List.of("EUR", "USD", "GBP");

            StringBuilder message = new StringBuilder("Balances: ");

            for (String currency : currencies) {
                BigDecimal balance = balanceService.getCurrentBalance(account, currency);
                if (balance != null) {
                    message.append(balance).append(" ").append(currency).append(", ");
                }
            }

            if (message.length() > "Balances: ".length()) {
                message.setLength(message.length() - 2);
                modelAndView.addObject("message", message.toString());
            } else {
                modelAndView.addObject("message", "Balances not found for this account.");
            }
        } else {
            modelAndView.addObject("message", "Account not found.");
        }

        return modelAndView;
    }

}
