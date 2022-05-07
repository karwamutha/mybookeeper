package com.example.mybookkeeper.managers;

public class ManagerTotal {
    private Manager manager;
    private double receiptsTotal;
    private double expensesTotal;

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
