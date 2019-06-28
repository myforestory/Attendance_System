package com.example.myapplication;

public class MainInfo {

    private int type;
    private String id;
    private String date;
    private String day;
    private String strTime;
    private String endTime;
    private String totalTime;
    private String discretion;

    public MainInfo() {

    }

    public MainInfo(int type, String id, String date, String day, String strTime, String endTime, String totalTime, String discretion) {
        this.type = type;
        this.id = id;
        this.date = date;
        this.day = day;
        this.strTime = strTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.discretion = discretion;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getDiscretion() {
        return discretion;
    }

    public void setDiscretion(String discretion) {
        this.discretion = discretion;
    }
}
