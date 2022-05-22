package com.example.mybookkeeper.accounts;

public class Account {
    private int accountId;
    private String accName;
    private int MgId;

    public Account(int accountId, String accName, int myMngId, int MgId) {
        this.accountId = accountId;
        this.accName = accName;
        this.MgId = MgId;
    }
    public Account(int accountId, String accName, int MgId) {
        this.accountId = accountId;
        this.accName = accName;
        this.MgId = MgId;
    }
    public Account(String accName, int MgId) {
        this.accName = accName;
        this.MgId = MgId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccName(String accName) {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public int getMgId() { return MgId; }

    public void setMgId(int MgId) { this.MgId = MgId;
    }

    public String getAccName() {
        return accName;
    }
}