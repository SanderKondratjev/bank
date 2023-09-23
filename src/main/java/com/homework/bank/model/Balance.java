package com.homework.bank.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long balance_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "balance_amount")
    private BigDecimal balance_amount;

    @Column(name = "balance_date")
    private Date balance_date;

    public Long getBalance_id() {
        return balance_id;
    }

    public void setBalance_id(Long balance_id) {
        this.balance_id = balance_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(BigDecimal balance_amount) {
        this.balance_amount = balance_amount;
    }

    public Date getBalance_date() {
        return balance_date;
    }

    public void setBalance_date(Date balance_date) {
        this.balance_date = balance_date;
    }
}

