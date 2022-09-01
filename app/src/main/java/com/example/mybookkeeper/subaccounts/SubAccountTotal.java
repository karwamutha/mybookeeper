package com.example.mybookkeeper.subaccounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class SubAccountTotal {
    @JsonUnwrapped
    private SubAccount subaccount;
    private double receiptsTotal;
    private double expensesTotal;
    private double balance;

    @JsonCreator
    public SubAccountTotal() {
    }

    public SubAccountTotal(SubAccount subaccount, double receiptsTotal, double expensesTotal) {
        this.subaccount = subaccount;
        this.receiptsTotal = receiptsTotal;
        this.expensesTotal = expensesTotal;
    }

    public SubAccount getSubAccount() {
        return subaccount;
    }

    public void setSubAccount(SubAccount subaccount) {
        this.subaccount = subaccount;
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