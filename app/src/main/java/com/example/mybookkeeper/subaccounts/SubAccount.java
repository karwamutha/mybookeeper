package com.example.mybookkeeper.subaccounts;

import com.example.mybookkeeper.data.SqliteDatabase;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubAccount {
    @JsonProperty(SqliteDatabase.SUBACCOUNT_NAME)
    private String subAccName;
    @JsonProperty(SqliteDatabase.SUBACCOUNT_ID)
    private int subAccId;
    @JsonProperty(SqliteDatabase.SUB_AC_ID)
    private int accId;
    @JsonProperty(SqliteDatabase.SUB_MG_ID)
    private int subMgId;

    public SubAccount() {
    }

    public SubAccount(int subAccId, String subAccName, int myMngId, int accid) {
        this.subAccId = subAccId;
        this.subAccName = subAccName;
        this.subMgId = myMngId;
        this.accId = accid;
    }
    public SubAccount(String subAccName, int subMgId, int accid) {
        this.subAccName = subAccName;
        this.subMgId = subMgId;
        this.accId = accid;
    }

    public String getSubAccName() {
        return subAccName;
    }

    public void setSubAccName(String subAccName) {
        this.subAccName = subAccName;
    }

    public int getSubAccId() {
        return subAccId;
    }

    public void setSubAccId(int subAccId) {
        this.subAccId = subAccId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public int getSubMgId() {
        return subMgId;
    }

    public void setSubMgId(int subMgId) {
        this.subMgId = subMgId;
    }
}