package com.example.mybookkeeper.managers;

import com.example.mybookkeeper.data.SqliteDatabase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Manager {

    @JsonProperty(SqliteDatabase.MANAGER_ID)
    private int managerId;
    @JsonProperty(SqliteDatabase.MANAGER_PHONE)
    private String managerPhone;
    @JsonProperty(SqliteDatabase.MANAGER_PASSWORD)
    private String managerPassword;
    @JsonProperty(SqliteDatabase.MANAGER_NAME)
    private String managerName;

    public Manager() {
    }

    public Manager(int managerId, String managerName, String managerPhone, String managerPassword) {
        this.managerId = managerId;
        this.managerPhone = managerPhone;
        this.managerPassword = managerPassword;
        this.managerName = managerName;
    }

    public Manager(String managerName, String managerPhone, String managerPassword) {
        this.managerPhone = managerPhone;
        this.managerPassword = managerPassword;
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public String getManagerName() {
        return managerName;
    }

    public int getManagerID() {
        return managerId;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
}
