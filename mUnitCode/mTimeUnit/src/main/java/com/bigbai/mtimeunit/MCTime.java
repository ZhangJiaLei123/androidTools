package com.bigbai.mtimeunit;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间工具
 */
public class MCTime {
    public static final int MODEL_YMD = 1;
    public static final int MODEL_HMS = 2;
    public static final int MODEL_DAY_WEEK = 3;
    public static final int MODEL_YMD_HMS = 4;
    public static final int MODEL_YMD_HMS_N = 5;

    public static String getTimeStr(int MODEL){
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//获取系统的日期
//年
        int year = calendarNow.get(Calendar.YEAR);
//月
        int month = calendarNow.get(Calendar.MONTH)+1;
//日
        int day = calendarNow.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendarNow.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendarNow.get(Calendar.MINUTE);
//秒
        int second = calendarNow.get(Calendar.SECOND);

        int weekda = calendarNow.get(Calendar.DAY_OF_WEEK);

        switch (MODEL){
            case MODEL_YMD:
                return String.format("%04d/%02d/%02d", year, month, day);
            case MODEL_HMS:
                return String.format("%02d:%02d:%02d", hour, minute, second);
            case MODEL_DAY_WEEK:
                return String.format("%s", getWeekStr(weekda));
            case MODEL_YMD_HMS:
                return String.format("%04d/%02d/%02d %02d:%02d:%02d", year, month, day,  hour, minute, second);
            case MODEL_YMD_HMS_N:
                return String.format("%04d/%02d/%02d\r\n%02d:%02d:%02d", year, month, day,  hour, minute, second);
            default:
                return String.format("%40d/%20d%20d", year, month, day);
        }
    }

    private static String getWeekStr(int wek) {
        switch (wek){
            case 1:return "星期日";
            case 2:return "星期一";
            case 3:return "星期二";
            case 4:return "星期三";
            case 5:return "星期四";
            case 6:return "星期五";
            case 7:return "星期六";
            default:
                return "ERROR";
        }
    }

}

