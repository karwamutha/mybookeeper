package com.example.mybookkeeper.fragmernts.receipts;

public class ReceiptData {

    private int rctId;
    private String date;
    private int rctNo;
    private int mgid;
    private int accId;
    private int subId;
    private int clientId;
    private String cltName;
    private double amount;

    public ReceiptData(int rctId, String date, int rctNo,
                       int mgid, int accId, int subId,
                       int clientId, String cltName, double amount) {
        this.rctId = rctId;
        this.date = date;
        this.rctNo = rctNo;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subId;
        this.clientId = clientId;
        this.cltName = cltName;
        this.amount = amount;
    }

    public ReceiptData(String date, int rctNo, int mgid,
                       int accId, int subaccId, int clientId, String cltName, double amt) {
        this.date = date;
        this.rctNo = rctNo;
        this.mgid = mgid;
        this.accId = accId;
        this.subId = subaccId;
        this.clientId = clientId;
        this.cltName = cltName;
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
    public void setCltName(String cltName) {
        this.cltName = cltName;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
