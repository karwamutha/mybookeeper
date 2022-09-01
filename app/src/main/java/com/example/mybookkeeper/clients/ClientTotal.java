package com.example.mybookkeeper.clients;

import com.example.mybookkeeper.fragmernts.TransactionDialogFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ClientTotal {
    @JsonUnwrapped
    private Client client;
    private double receiptsTotal;
    private double expensesTotal;
    private double balance;

    @JsonCreator
    public ClientTotal() {
    }

    public ClientTotal(Client client, double receiptsTotal, double expensesTotal, double balance) {
        this.client = client;
        this.receiptsTotal = receiptsTotal;
        this.expensesTotal = expensesTotal;
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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