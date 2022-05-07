package com.example.mybookkeeper.subaccounts;

public class SubAccount {
     private int subaccountId;
     private String subaccountName;
     private int subMgId;
     private int subaccId;

     public SubAccount(int subaccountId, String subaccountName, int subMgId, int subaccId) {
            this.subaccountId = subaccountId;
            this.subaccountName = subaccountName;
            this.subMgId = subMgId;
            this.subaccId = subaccId;
        }
     public SubAccount(int client_id, String subaccountName, int myClientMngId, int subMgId, int subaccId) {
        this.subaccountName = subaccountName;
        this.subMgId = subMgId;
        this.subaccId = subaccId;
    }
        public int getSubAccountId() {
            return subaccountId;
        }
        public String getSubAccName() {
            return subaccountName;
        }
        public int getSubMgId() {
            return subMgId;
        }
        public int getAccId() {
            return subaccId;
        }
        public void setSubAccountName(String subaccountName) {
            this.subaccountName = subaccountName;
        }
        public void setSubMgId(int subMgId) {
            this.subMgId = subMgId;
        }
        public void setSubAccId(int subaccId) {
    this.subaccId = subaccId;
    }
}
