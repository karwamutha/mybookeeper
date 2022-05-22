package com.example.mybookkeeper.fragmernts.expenses;

public class ExpenseData {

    private int expId;
    private String date;
    private int expNo;
    private int mgid;
    private int accId;
    private int subId;
    private int clientId;
    private String cltName;
    private String descr;
    private double amt;

    public ExpenseData(int expId, String date, int expNo, int mgid,
                       int accId, int subId, int clientId, String cltName,
                       String descr, double amt) {
        this.expId = expId;
        this.expNo = expNo;
        this.date = date;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subId;
        this.clientId = clientId;
        this.cltName = cltName;
        this.descr = descr;
        this.amt = amt;
    }

    public ExpenseData(String date, int expNo, int mgid, int accId,
                       int subaccId, int clientId, String cltName, String descr, double amt) {
        this.expNo = expNo;
        this.date = date;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subaccId;
        this.clientId = clientId;
        this.cltName = cltName;
        this.descr = descr;
        this.amt = amt;
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

    public String getCltName() {
        return cltName;
    }
    public void setCltName(String cltName) { this.cltName = cltName; }

    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) { this.cltName = descr; }

    public double getAmount() {
        return amt;
    }

    public void setAmount(double amt) {
        this.amt = amt;
    }

}
