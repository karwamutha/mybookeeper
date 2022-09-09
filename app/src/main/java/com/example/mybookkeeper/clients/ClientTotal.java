package com.example.mybookkeeper.clients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ClientTotal {
    @JsonUnwrapped
    private Client client;
    private double ReceiptsTotal;
    private double ExpensesTotal;
    private double balance;

    @JsonCreator
    public ClientTotal() {
    }

    public ClientTotal(Client client, double receiptsTotal, double expensesTotal, double balance) {
        this.client = client;
        this.ReceiptsTotal = receiptsTotal;
        this.ExpensesTotal = expensesTotal;
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getReceiptsTotal() {
        return ReceiptsTotal;
    }

    public void setReceiptsTotal(double receiptsTotal) {
        this.ReceiptsTotal = receiptsTotal;
    }

    public double getExpensesTotal() {
        return ExpensesTotal;
    }

    public void setExpensesTotal(double expensesTotal) {
        this.ExpensesTotal = expensesTotal;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}