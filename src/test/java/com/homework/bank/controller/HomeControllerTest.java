package com.homework.bank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    @Test
    public void testHome() {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.home();

        assertEquals("index", modelAndView.getViewName());
    }

    @Test
    public void testOpenAccountForm() {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.openAccountForm();

        assertEquals("open-account", modelAndView.getViewName());
    }

    @Test
    public void testTransactionsForm() {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.transactionsForm();

        assertEquals("transactions", modelAndView.getViewName());
    }

    @Test
    public void testBalancesForm() {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.balancesForm();

        assertEquals("balances", modelAndView.getViewName());
    }

    @Test
    public void testAccountStatementForm() {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = homeController.accountStatementForm();

        assertEquals("account-statement", modelAndView.getViewName());
    }
}
