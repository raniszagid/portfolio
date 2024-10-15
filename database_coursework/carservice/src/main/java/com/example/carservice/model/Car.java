package com.example.carservice.model;

public class Car {
    private int id;
    private int num;
    private String mark;
    private String color;
    private boolean isForeign;
    private String boolStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getIsForeign() {
        return isForeign;
    }

    public void setForeign(boolean foreign) {
        isForeign = foreign;
    }

    public String getBoolStr() {
        return boolStr;
    }

    public void setBoolStr(String boolStr) {
        this.boolStr = boolStr;
        if (boolStr.equals("true")) isForeign = true;
        else if (boolStr.equals("false")) isForeign = false;
    }

    public String getForeignString() {
        return getIsForeign() ? "foreign" : "Russian";
    }
}
