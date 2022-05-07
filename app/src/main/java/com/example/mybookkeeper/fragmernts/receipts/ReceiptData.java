package com.example.mybookkeeper.fragmernts.receipts;

public class ReceiptData {

    private int rctId;
    private String date;
    private int rctNo;
    private String subname;
    private int mgid;
    private int accId;
    private int subId;
    private int clientId;
    private double amount;

    public ReceiptData(int rctId, String date, int rctNo, String subname,
                       int mgid, int accId, int subId,
                       int clientId, double amount) {
        this.rctId = rctId;
        this.date = date;
        this.rctNo = rctNo;
        this.subname = subname;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subId;
        this.clientId = clientId;
        this.amount = amount;
    }

    public ReceiptData(String date, int rctNo, String mngName, int mgid,
                       int accId, int subaccId, int clientId, double amt) {
        this.date = date;
        this.rctNo = rctNo;
        this.subname = subname;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subaccId;
        this.clientId = clientId;
        this.amount = amt;
    }

    public int getRctID() {
        return rctId;
    }
    public void setRctID(int rctId) {
        this.rctId = rctId;
    }
    public int getRctNo() {
        return rctNo;
    }
    public void setRctNo(int rctNo) {
        this.rctNo = rctNo;
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
