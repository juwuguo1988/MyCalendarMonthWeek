package cn.jwg.mycalendarmonthweek.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间工具类
 */
public class TimeUtils {

    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    public static final SimpleDateFormat DATE_SDF_YMDHMS = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    public static final SimpleDateFormat DATE_SDF_YMDHM = new SimpleDateFormat("yyyy:MM:dd HH:mm");
    public static final SimpleDateFormat DATE_SDF_YMD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat HOUR_SDF = new SimpleDateFormat("HH");
    public static final SimpleDateFormat WEEK_SDF = new SimpleDateFormat("EEE");

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(format.parse(pTime));
        }catch (Exception e){
            e.printStackTrace();
        }
        int dayForWeek;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 0;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    //获取前天的date
    public static String getBeforeYesdayDateForRequest() {
        String yesDate = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        yesDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return yesDate;
    }


    /**
     * 获取当前月份第一天
     */
    public static Calendar getYMDCalendar(int originalWeek, int year, int month, int day) {
        int newWeek = dayForWeek(year + String.format("-%02d", month + 1) + String.format("-%02d", day));
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.MONTH, month);
        ca.set(Calendar.DAY_OF_MONTH, day + (originalWeek - newWeek));//设置为1号
        return ca;
    }

}
