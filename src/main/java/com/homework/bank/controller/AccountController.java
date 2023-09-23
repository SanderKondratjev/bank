package com.homework.bank.controller;

import com.homework.bank.model.Account;
import com.homework.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/open")
    public ModelAndView openAccount(@RequestBody String accountName) {
        Account account = accountService.openAccount(accountName);

        ModelAndView modelAndView = new ModelAndView("account-details");
        modelAndView.addObject("accountName", account.getAccountName());
        modelAndView.addObject("accountNumber", account.getIban());

        return modelAndView;
    }
}
