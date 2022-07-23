package com.example.mybookkeeper.fragmernts.expenses;

import com.example.mybookkeeper.data.SqliteDatabase;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpenseData {

    @JsonProperty(SqliteDatabase.EXPENSE_ID)
    private int expId;
    @JsonProperty(SqliteDatabase.EXPENSE_DATE)
    private String date;
    @JsonProperty(SqliteDatabase.EXPENSE_NO)
    private int expNo;
    @JsonProperty(SqliteDatabase.EXP_MG_ID)
    private int mgid;
    @JsonProperty(SqliteDatabase.EXP_ACC_ID)
    private int accId;
    @JsonProperty(SqliteDatabase.EXP_SUBAC_ID)
    private int subId;
    @JsonProperty(SqliteDatabase.EXP_CLIENT_ID)
    private int clientId;
    @JsonProperty(SqliteDatabase.EXP_CLIENT_NAME)
    private String cltName;
    @JsonProperty(SqliteDatabase.DESCRIPTION)
    private String descr;
    @JsonProperty(SqliteDatabase.EXPENSE_AMOUNT)
    private double amt;

    public ExpenseData() {
    }

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

    public void setCltName(String cltName) {
        this.cltName = cltName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public double getAmount() {
        return amt;
    }

    public void setAmount(double amt) {
        this.amt = amt;
    }

    public int getExpId() {
        return expId;
    }

    public void setExpId(int expId) {
        this.expId = expId;
    }

    public int getMgid() {
        return mgid;
    }

    public void setMgid(int mgid) {
        this.mgid = mgid;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }
}