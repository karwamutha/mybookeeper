package com.example.mybookkeeper.managers;

public class Manager {
    private int managerId;
    private String managerPhone;
    private String managerPassword;
    private String managerName;

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
    public void setManagerPassword(String managerPassword) { this.managerPassword = managerPassword; }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

}
