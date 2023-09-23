package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Transactional
    public Balance createBalance(Account account) {
        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setBalanceAmount(BigDecimal.ZERO);
        balance.setBalanceDate(new Date());
        return balanceRepository.save(balance);
    }

    public Balance getBalanceByAccount(Account account) {
        return balanceRepository.findByAccount(account);
    }

    @Transactional
    public void updateBalance(Balance balance) {
        balanceRepository.save(balance);
    }

    public Balance getBalanceByAccountAndCurrency(Account account, String currency) {
        return balanceRepository.findByAccountAndCurrency(account, currency);
    }
}
