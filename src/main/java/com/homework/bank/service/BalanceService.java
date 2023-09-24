package com.homework.bank.service;

import com.homework.bank.model.Account;
import com.homework.bank.model.Balance;
import com.homework.bank.repository.BalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class BalanceService {

    private static final Logger log = LoggerFactory.getLogger(BalanceService.class);

    @Autowired
    private BalanceRepository balanceRepository;

    public void updateBalance(Account account, String currency, BigDecimal amount) {
        Balance newBalance = new Balance();
        newBalance.setAccount(account);
        newBalance.setCurrency(currency);
        newBalance.setBalanceAmount(amount.add(getCurrentBalance(account, currency)));
        newBalance.setBalanceDate(new Date());
        balanceRepository.save(newBalance);

        log.info("Balance before transaction: {}. Balance after transaction: {}. Transaction was {}. For account number: {}.",
                getCurrentBalance(account, currency),
                newBalance.getBalanceAmount(),
                getTransactionType(amount),
                account.getIban()
        );
    }

    public BigDecimal getCurrentBalance(Account account, String currency) {
        Balance balance = balanceRepository.findTop1ByAccountAndCurrencyOrderByBalanceDateDesc(account, currency);

        if (balance == null) {
            return BigDecimal.ZERO;
        }

        return balance.getBalanceAmount();
    }

    public Balance getBalanceByAccount(Account account) {
        return balanceRepository.findByAccount(account);
    }

    public String getTransactionType(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0 ? "Withdraw"
                : amount.compareTo(BigDecimal.ZERO) > 0 ? "Deposit"
                : "No Transaction";
    }
}
