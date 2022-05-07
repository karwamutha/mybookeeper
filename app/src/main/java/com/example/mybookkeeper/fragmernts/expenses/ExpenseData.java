package com.example.mybookkeeper.fragmernts.expenses;

public class ExpenseData {

    private int expId;
    private String date;
    private int expNo;
    private String subname;
    private int mgid;
    private int accId;
    private int subId;
    private int clientId;
    private double amount;

    public ExpenseData(int expId, String date, int expNo, String subname,
                       int mgid, int accId, int subId,
                       int clientId, double amount) {
        this.expId = expId;
        this.expNo = expNo;
        this.subname = subname;
        this.date = date;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subId;
        this.clientId = clientId;
        this.amount = amount;
    }

    public ExpenseData(String date, int expNo, String subname, int mgid, int accId, int subaccId, int clientId, double amt) {
        this.expNo = expNo;
        this.subname = subname;
        this.date = date;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subaccId;
        this.clientId = clientId;
        this.amount = amt;
    }

    public int getExpID() {
        return expId;
    }
    public void setExpID(int expId) {
        this.expId = expId;
    }
    public int getExpNo() {
        return expNo;
    }
    public void setExpNo(int expNo) {
        this.expNo = expNo;
    }
    public String getSubname() {
        return subname;
    }
    public void setSubname(String subname) {
        this.subname = subname;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getMgId() {
        return mgid;
    }
    public void setMgId(int mgid) {
        this.mgid = mgid;
    }
    public int getAccId() {
        return accId;
    }
    public void setAccId(int accId) {
        this.accId = accId;
    }
    public int getSubId() {
        return subId;
    }
    public void setSubId(int subId) {
        this.subId = subId;
    }
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
