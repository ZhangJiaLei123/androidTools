package com.bigbai.mtimeunit;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间工具
 */
public class CalendarUnit {
    public static final int onelyTime = 0;
    public static final int onelyDate = 2;


    public static String getTime(int Model){
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        switch (Model){
            case onelyTime:
                return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + ":" + cal.get(Calendar.MILLISECOND);

            case onelyDate:
                return cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + ":" + cal.get(Calendar.MILLISECOND);

                default:
                    return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + ":" + cal.get(Calendar.MILLISECOND);

        }

    }
}
