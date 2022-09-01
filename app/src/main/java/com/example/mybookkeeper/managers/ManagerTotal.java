package com.example.mybookkeeper.managers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ManagerTotal {
    @JsonUnwrapped
    private Manager manager;
    private double receiptsTotal;
    private double expensesTotal;
    private double balance;

    @JsonCreator
    public ManagerTotal() {
    }

    public ManagerTotal(Manager manager, double receiptsTotal, double expensesTotal) {
        this.manager = manager;
        this.receiptsTotal = receiptsTotal;
        this.expensesTotal = expensesTotal;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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
//public class ManagerTotal {
//    private Manager manager;
//    private double total;
//
//    public ManagerTotal(Manager manager, double total) {
//        this.manager = manager;
//        this.total = total;
//    }
//
//    public Manager getManager() {
//        return manager;
//    }
//
//    public void setManager(Manager manager) {
//        this.manager = manager;
//    }
//
//    public double getTotal() {
//        return total;
//    }
//
//    public void setTotal(double total) {
//        this.total = total;
//    }
//}
