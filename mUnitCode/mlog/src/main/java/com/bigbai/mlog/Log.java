package com.bigbai.mlog;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            return ReadTxtFile(name);
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

        writeBySd(LogPath,"<" +my_time_1 + "-" +  my_time_2 + " [" + model + "]>--" +  msg + "\n",true);

    }

    public static void clearLog(){
        writeBySd(LogPath,"",false);

    }
    public  static boolean writeBySd(String filename, String filecontent) {
        return writeBySd(filename,filecontent,false);
    }
    /**
     * 文件写入到 Sd 卡根目录
     * @param filename
     * @param filecontent
     * @param isAdd 是否追加
     * @return
     */
    public  static boolean writeBySd(String filename, String filecontent,boolean isAdd) {
        try {
            //如果手机已插入sd卡,且app具有读写sd卡的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename,isAdd);
                output.write(filecontent.getBytes());
                //将String字符串以字节流的形式写入到输出流中
                output.close();
                LogPath = filename; // 保存路径到缓存
                //关闭输出流
                return true;
            }
        }catch (Exception e){
            LogPath = null;
            return false;
        }
        LogPath = null;
        return false;

    }

    /**
     * 读
     * @param strFilePath
     * @return
     */
    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            d("测试", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                d("测试", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                d("测试", "error:" + e.getMessage());
            }
        }
        return content;
    }

}
