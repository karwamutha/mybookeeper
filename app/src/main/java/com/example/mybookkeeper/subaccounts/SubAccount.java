package com.example.mybookkeeper.subaccounts;

public class SubAccount {
    private String subAccName;
    private int subAccId;
    private int accId;
    private int subMgId;

    public SubAccount(int subAccId, String subAccName, int myMngId, int accid) {
        this.subAccId = subAccId;
        this.subAccName = subAccName;
        this.subMgId = subMgId;
        this.accId = accId;
    }
    public SubAccount(String subAccName, int subMgId, int accid) {
        this.subAccName = subAccName;
        this.subMgId = subMgId;
        this.subAccId = accid;
    }
    public SubAccount(String subAccName, int subMgId) {
        this.subAccName = subAccName;
        this.subMgId = subMgId;
    }

    public int getsubAccId(int accId) {
        return subAccId;
    }

    public void setsubAccId(int subAccId) {
        this.subAccId = subAccId;
    }

    public String getSubAccName(String subAccName) {
        return subAccName;
    }

    public void setSubAccName(String subAccName) {
        this.subAccName = subAccName;
    }

    public int getSubMgId(int mgid) { return subMgId; }

    public void setSubMgId(int subMgId) { this.subMgId = subMgId;
    }

    public String getSubAccName() {
        return subAccName;
    }

    public int getsubAccId() {
        return subAccId;
    }

    public int getSubMgId() {
        return subMgId;
    }
}