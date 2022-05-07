package com.example.mybookkeeper.home;

public class HomeData{
    private String homeName;
    private int imgId;
    private final int navigationId;

    public HomeData(String homeName, int imgId, int navigationId) {
        this.homeName=homeName;
        this.imgId=imgId;
        this.navigationId = navigationId;
    }

    public String getMyHomeName() {
        return homeName;
    }

    public void setMyHomeName(String description) {
        this.homeName = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getNavigationId() {
        return navigationId;
    }
}
