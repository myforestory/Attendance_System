package com.example.myapplication;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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

    public static String[] subMonth(String date){
        String[] reStrArray = new String[0];
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(date);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.MONTH, -1);
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            reStrArray = new String[]{reStr.split("-")[0], reStr.split("-")[1]};
            return reStrArray;
        } catch (Exception e) {

        }
        return reStrArray;
    }

    public static String plusOneDay(String dateAndTime, String hourAndMin){
        String todayAsString = dateAndTime;
        try {
            SimpleDateFormat hourAndMinFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date zero = hourAndMinFormat.parse("00:00:00");
            Date six = hourAndMinFormat.parse("06:00:00");
            Date userTime = hourAndMinFormat.parse(hourAndMin);
            Date userDate = dateFormat.parse(dateAndTime);
            Date plusOneDay = new Date();
            if (userTime.compareTo(zero) == 0 || ( userTime.after(zero) && userTime.before(six))) {
                Calendar c = Calendar.getInstance();
                c.setTime(userDate);
                c.add(Calendar.DATE, 1);
                plusOneDay = c.getTime();
                todayAsString = dateFormat.format(plusOneDay);
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return todayAsString;
    }

    public static Boolean isAfterTime(String getTime, String compareTime) {
        Boolean isAfter = false;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date getTimeFormat = sdf.parse(getTime);
            Date compareTimeFormat = sdf.parse(compareTime);
            if (getTimeFormat.compareTo(compareTimeFormat) > 0) {
                isAfter = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isAfter;
    }
}
