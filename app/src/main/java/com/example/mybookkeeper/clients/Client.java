package com.example.mybookkeeper.clients;

public class Client {
    private int Id;
    private String cltName;
    private int CltMgid;
    private int cltAccid;
    private int cltSubId;

    public Client(int id, String cltName, int CltMgid, int cltAccid, int cltSubId) {
        this.Id = id;
        this.cltName = cltName;
        this.CltMgid = CltMgid;
        this.cltAccid = cltAccid;
        this.cltSubId = cltSubId;
    }

    public Client(String cltName, int CltMgid, int cltAccid, int cltSubId) {
        this.cltName = cltName;
        this.CltMgid = CltMgid;
        this.cltAccid = cltAccid;
        this.cltSubId = cltSubId;
    }

    public int getId() {
        return Id;
    }

    public String getCltName() {
        return cltName;
    }

    public void setCltName(String cltName) {
        this.cltName = cltName;
    }

    public int getCltMgid() {
        return CltMgid;
    }

    public void setCltMgid(int CltMgid) {
        this.CltMgid = CltMgid;
    }

    public int getCltAccid() {
        return cltAccid;
    }

    public void setCltAccid(int cltAccid) {
        this.cltAccid = cltAccid;
    }

    public int getCltSubId() {
        return cltSubId;
    }

    public void setCltSubId(int cltSubId) {
        this.cltSubId = cltSubId;
    }
}
