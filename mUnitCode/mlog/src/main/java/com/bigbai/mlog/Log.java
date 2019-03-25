package com.bigbai.mlog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * 重写Log，添加自定义控制
 * 简便的日志读写操作
 */
public class LOG {
    /** 默认TAG */
    public static String TAG = "测试";
    /** 是否保存 */
    public static boolean isSave = false;
    /** SD*/
    public static String SDPath;

    static {
        try {
            SDPath = Environment.getExternalStorageDirectory().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /** 保存路径*/
    public static String LogPath = "日志.log";


    /** 是否允许输出显示日志 */
    public static Boolean isLog = true;
    /** 是否允许输出显示信息 */
    public static Boolean isInfo = true;
    /** 是否允许输出显示调试 */
    public static Boolean isDebug = true;
    /** 是否允许输出显示错误 */
    public static Boolean isError = true;
    /** 是否允许输出显示警告 */
    public static Boolean isWarning = true;

    /**
     * 信息
     * @param mag
     */
    public static void i(String TAG,String mag)
    {
        if(isInfo && isLog){
            android.util.Log.i(TAG, "" + mag);
            writeLog(TAG + "" + mag ,"INFO");
        }

    }

    /**
     * 信息
     * @param mag
     */
    public static void i(String mag)
    {
        if(isInfo && isLog && mag!= null){
            i(TAG,mag);
        }

    }

    /**
     * 调试
     * @param mag
     */
    public static void d(String TAG,String mag)
    {
        if(isDebug && isLog){
            android.util.Log.d(TAG,mag);
            writeLog(TAG + "" + mag ,"DEBUG");
        }

    }

    /**
     * 调试
     * @param mag
     */
    public static void d(String mag)
    {
        if(isDebug && isLog) {
            d(TAG, mag);
        }
    }

    /**
     * 错误
     * @param mag
     */
    public static void e(String TAG,String mag)
    {
        if(isError && isLog){
            android.util.Log.e(TAG,mag);
            writeLog(TAG + "" + mag ,"ERROR");
        }

    }

    /**
     * 错误
     * @param mag
     */
    public static void e(String mag)
    {
        if(isError && isLog){
            e(TAG,mag);
        }

    }

    /**
     * 警告
     * @param mag
     */
    public static void w(String TAG,String mag)
    {
        if(isWarning && isLog){
            android.util.Log.w(TAG,mag);
            writeLog(TAG + "" + mag ,"WARNING");
        }

    }

    /**
     * 警告
     * @param mag
     */
    public static void w(String mag)
    {
        if(isWarning && isLog) {
            w(TAG, mag);
        }
    }

    /**
     * 获取log文件
     * @return
     */
    public static String getLog(){
        try {
            String name = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + LogPath;
            return ReadTxtFile(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }

    /**
     * 保存日志到SD卡
     * */
    private static void writeLog(String msg,String model){

        if (!isSave){
            return;
        }

        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String hour;
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH))+1;
        String day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0) {
            hour = String.valueOf(cal.get(Calendar.HOUR));
        }
        else {
            hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
        }
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        String second = String.valueOf(cal.get(Calendar.SECOND));

        String my_time_1 = year + "/" + month + "/" + day;
        String my_time_2 = hour + ":" + minute + ":" + second;

        writeBySd(LogPath,"<" +my_time_1 + "-" +  my_time_2 + " [" + model + "]>--" +  msg + "\n",true);

    }

    /**
     * 清除日志文件
     * */
    public static void clearLog(){
        writeBySd(LogPath,"",false);

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
                filename = SDPath + "/" + filename;
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename,isAdd);
                output.write(filecontent.getBytes());
                //将String字符串以字节流的形式写入到输出流中
                output.close();
               // LogPath = filename; // 保存路径到缓存
                //关闭输出流
                return true;
            }
        }catch (Exception e){
            LogPath = null;
            d(TAG, "写入日志失败:路径为空");
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
    public static String ReadTxtFile(String strFilePath) {

        String path = SDPath + "/" + strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            d(TAG, "The File doesn't not exist.");
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
                d(TAG, "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                d(TAG, "error:" + e.getMessage());
            }
        }
        return content;
    }

    /**
     * 在对话框中显示日志
     * @param context
     */
    public static void showLogDialog(Context context){
        String log = ReadTxtFile(LogPath);

        //创建一个对话框对象
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        //对对话框内容进行定义
        builder.setTitle("日志");
        builder.setMessage(log);
        //定义对话框内容的点击事件,注意后面还有个show，否则不会显示对话框
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            //定义点击对话框按钮后执行的动作，这里是添加一个textview的内容
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener(){
            @Override
            //定义点击对话框按钮后执行的动作，这里是添加一个textview的内容
            public void onClick(DialogInterface dialog, int which) {
                clearLog();
            }
        }).show();

        builder.setNeutralButton("发送", new DialogInterface.OnClickListener(){
            @Override
            //定义点击对话框按钮后执行的动作，这里是添加一个textview的内容
            public void onClick(DialogInterface dialog, int which) {
                // 发送日志到邮箱
            }
        }).show();
    }

}
