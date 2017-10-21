package cn.jwg.mycalendarmonthweek.bean;

import java.io.Serializable;

/**
 * Created by Jimmy on 2016/10/8 0008.
 */
public class CalendarBean implements Serializable {

    private int color;
    private int day;


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
