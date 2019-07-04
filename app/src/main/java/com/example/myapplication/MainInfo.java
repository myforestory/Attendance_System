package com.example.myapplication;

public class MainInfo {

    private int type;
    private String id;
    private String date;
    private String day;
    private String start;
    private String end;
    private String worked_time;
    private String remarks;
    private String month;
    private String year;


    public MainInfo() {

    }

    public MainInfo(int type, String id, String date, String day, String start, String end, String worked_time, String remarks, String month, String year) {
        this.type = type;
        this.id = id;
        this.date = date;
        this.day = day;
        this.start = start;
        this.end = end;
        this.worked_time = worked_time;
        this.remarks = remarks;
        this.month = month;
        this.year = year;
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

    public String getDate() {
        return date;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getWorked_time() {
        return worked_time;
    }

    public void setWorked_time(String worked_time) {
        this.worked_time = worked_time;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}