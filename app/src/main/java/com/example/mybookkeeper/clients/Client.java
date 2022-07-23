package com.example.mybookkeeper.clients;

import com.example.mybookkeeper.data.SqliteDatabase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Client {
    @JsonProperty(SqliteDatabase.CLIENT_ID)
    private int Id;
    @JsonProperty(SqliteDatabase.CLIENT_NAME)
    private String cltName;
    @JsonProperty(SqliteDatabase.CLIENT_MG_ID)
    private int CltMgid;
    @JsonProperty(SqliteDatabase.CLIENT_AC_ID)
    private int cltAccid;
    @JsonProperty(SqliteDatabase.CLIENT_SUBAC_ID)
    private int cltSubId;

    @JsonCreator
    public Client() {
    }

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

    @JsonGetter(SqliteDatabase.CLIENT_ID)
    public int getId() {
        return Id;
    }

    @JsonSetter(SqliteDatabase.CLIENT_ID)
    public void setId(int id) {
        Id = id;
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
