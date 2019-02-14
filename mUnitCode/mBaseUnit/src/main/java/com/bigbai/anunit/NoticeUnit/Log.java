package com.bigbai.anunit.NoticeUnit;

import android.os.Environment;

import com.bigbai.anunit.mFileUnit.FileUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class Log {
    public static String TAG = "测试";
    /** 是否保存 */
    public static boolean isSave = true;
    /** 保存路径*/
    public static String LogPath = "日志.log";

    public static Boolean isLog = true;
    public static Boolean isInfo = true;
    public static Boolean isDebug = true;
    public static Boolean isError = true;
    public static Boolean isWarning = true;

    public static void i(String TAG,String mag)
    {
        if(isInfo && isLog){
            android.util.Log.i(TAG, "" + mag);
            if(isSave){
                writeLog(TAG + "" + mag ,"INFO");
              //  FileUtils.writeBySd(LogPath,TAG + "" + mag + "\n",true);
            }
        }

    }

    public static void i(String mag)
    {
        if(isInfo && isLog && mag!= null){
            i(TAG,mag);
        }

    }

    public static void d(String TAG,String mag)
    {
        if(isDebug && isLog){
            android.util.Log.d(TAG,mag);
            if(isSave){
                writeLog(TAG + "" + mag ,"DEBUG");
               // FileUtils.writeBySd(LogPath,TAG + "" + mag + "\n",true);
            }
        }

    }

    public static void d(String mag)
    {
        if(isDebug && isLog)
            d(TAG,mag);
    }

    public static void e(String TAG,String mag)
    {
        if(isError && isLog){
            android.util.Log.e(TAG,mag);
            if(isSave){
                writeLog(TAG + "" + mag ,"ERROR");
            }
        }

    }

    public static void e(String mag)
    {
        if(isError && isLog)
            e(TAG,mag);
    }

    public static void w(String TAG,String mag)
    {
        if(isWarning && isLog){
            android.util.Log.w(TAG,mag);
            if(isSave){
                writeLog(TAG + "" + mag ,"WARNING");
            }
        }

    }

    public static void w(String mag)
    {
        if(isWarning && isLog)
            w(TAG,mag);
    }

    public static String getLog(){
        try {
            String name = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + LogPath;
            return FileUtils.ReadTxtFile(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }

    private static void writeLog(String msg,String model){
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String hour;
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH))+1;
        String day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else
            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        String second = String.valueOf(cal.get(Calendar.SECOND));

        String my_time_1 = year + "/" + month + "/" + day;
        String my_time_2 = hour + ":" + minute + ":" + second;

        FileUtils.writeBySd(LogPath,"<" +my_time_1 + "-" +  my_time_2 + " [" + model + "]>--" +  msg + "\n",true);

    }

    public static void clearLog(){
        FileUtils.writeBySd(LogPath,"",false);

    }
}
