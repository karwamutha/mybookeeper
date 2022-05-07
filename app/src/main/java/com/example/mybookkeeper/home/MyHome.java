package com.example.mybookkeeper.home;


public class MyHome{
    private String myHomeName;
    private int imgId;
    private boolean isChecked;
    private final int navigationId;

    public MyHome(String homeName, int imgId, String isChecked, int navigationId) {
        this.myHomeName=homeName;
        this.imgId=imgId;
        //this.isChecked=isChecked;
        this.navigationId = navigationId;
    }

    public String getMyHomeName() {
        return myHomeName;
    }

    public void setMyHomeName(String description) {
        this.myHomeName = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getNavigationId() {
        return navigationId;
    }
}
