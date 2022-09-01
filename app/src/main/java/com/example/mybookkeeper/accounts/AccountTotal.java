package com.example.mybookkeeper.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class AccountTotal {
    @JsonUnwrapped
    private Account account;
    private double receiptsTotal;
    private double expensesTotal;
    private double balance;

    @JsonCreator
    public AccountTotal() {
    }

    public AccountTotal(Account account, double receiptsTotal, double expensesTotal) {
        this.account = account;
        this.receiptsTotal = receiptsTotal;
        this.expensesTotal = expensesTotal;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getReceiptsTotal() {
        return receiptsTotal;
    }

    public void setReceiptsTotal(double receiptsTotal) {
        this.receiptsTotal = receiptsTotal;
    }

    public double getExpensesTotal() {
        return expensesTotal;
    }

    public void setExpensesTotal(double expensesTotal) {
        this.expensesTotal = expensesTotal;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}