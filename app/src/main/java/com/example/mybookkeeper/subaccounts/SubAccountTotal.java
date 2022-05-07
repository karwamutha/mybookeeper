package com.example.mybookkeeper.subaccounts;

public class SubAccountTotal {
    private SubAccount subaccount;
    private double receiptsTotal;
    private double expensesTotal;

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
}