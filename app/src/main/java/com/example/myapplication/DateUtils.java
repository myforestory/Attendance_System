package com.example.myapplication;

import java.util.Calendar;

public class DateUtils {

    public static String timeFormattedRoundDown(String originalTime) {
        String formattedTime = "";
        String formattedMin = "";
        String hour = originalTime.split(":")[0];
        int originalMin = Integer.valueOf(originalTime.split(":")[1]);

        if (originalMin>=0 && originalMin<15) {
            formattedMin = "00";
        } else if (originalMin>=15 && originalMin<30) {
            formattedMin = "15";
        } else if (originalMin>=30 && originalMin<45) {
            formattedMin = "30";
        } else {
            formattedMin = "45";
        }
        formattedTime = hour + ":" + formattedMin;
        return formattedTime;
    }

    public static String getJpWeekday (String infoDate) {
        String weekday = "";
        int year, month, date;
        year = Integer.valueOf(infoDate.split("-")[0]);
        month = Integer.valueOf(infoDate.split("-")[1]) - 1;
        date = Integer.valueOf(infoDate.split("-")[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                weekday = "日";
                break;
            case Calendar.MONDAY:
                weekday = "月";
                break;
            case Calendar.TUESDAY:
                weekday = "火";
                break;
            case Calendar.WEDNESDAY:
                weekday = "水";
                break;
            case Calendar.THURSDAY:
                weekday = "木";
                break;
            case Calendar.FRIDAY:
                weekday = "金";
                break;
            case Calendar.SATURDAY:
                weekday = "土";
                break;
        }
        return weekday;
    }
}
