package com.bigbai.mtimeunit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * 获取时间
     * @return
     */
    public static Long getTime(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

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


    /**
     * 获取时间描述，xxx时间前
     *
     * @param time the time
     * @return the string
     */
    public static String friendlyTime(long time) {
        return getTimeDescribe(new Date(time));
    }
    /**
     * 获取时间描述，xxx时间前
     *
     * @param time the time
     * @return the string
     */
    public static String getTimeDescribe(Date time) {

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int inter = (int) (cal.getTimeInMillis() - time.getTime()) / 60000;
            int hour = inter / 60;
            if (inter == 0) {
                ftime = "刚刚";
            } else if (hour == 0) {
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days < 365) {
            ftime = dateFormater2.get().format(time);
        }
        else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        }
        else {
            ftime = dateFormater3.get().format(time);
        }
        return ftime;
    }


    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
}

