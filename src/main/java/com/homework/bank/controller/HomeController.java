package com.homework.bank.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
public class HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @GetMapping("/open-account")
    public ModelAndView openAccountForm() {
        return new ModelAndView("open-account");
    }

    @GetMapping("/transactions")
    public ModelAndView transactionsForm() {
        return new ModelAndView("transactions");
    }

    @GetMapping("/balances")
    public ModelAndView balancesForm() {
        return new ModelAndView("balances");
    }

    @GetMapping("/account-statement")
    public ModelAndView accountStatementForm() {
        return new ModelAndView("account-statement");
    }
}
