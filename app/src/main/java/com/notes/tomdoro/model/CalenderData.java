package com.notes.tomdoro.model;

/**
 * Created by C.M on 15/10/2018.
 */

public class CalenderData {
    public String userId;
    public String name;
    public String month;
    public String day;
    public String year;

    public String hour;
    public String minis;


    public CalenderData() {
    }

    public CalenderData( String userId, String name, String month, String day, String year, String hour, String minis) {
        this.userId = userId;
        this.name = name;
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.minis = minis;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinis() {
        return minis;
    }

    public void setMinis(String minis) {
        this.minis = minis;
    }

    @Override
    public String toString() {
        return "CalenderData{" +
                "name='" + name + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", year='" + year + '\'' +
                ", hour='" + hour + '\'' +
                ", minis='" + minis + '\'' +
                '}';
    }
}
