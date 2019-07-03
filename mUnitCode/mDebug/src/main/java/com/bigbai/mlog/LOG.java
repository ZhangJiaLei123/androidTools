package com.bigbai.mlog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 重写Log，添加自定义控制
 * 简便的日志读写操作
 */
public class LOG {

    private final static String INFO    = "[INFO]   ";
    private final static String DENUG   = "[DENUG]  ";
    private final static String ERROR   = "[ERROR]  ";
    private final static String WARNING = "[WARNING]";

    /** 默认TAG */
    public static String TAG = "日志";
    /** 是否保存 */
    public static boolean isSave = false;
    /** 日志保存文件 */
    private static File logFile = null;
    /** 日志显示视图 */
    private static View logView = null;
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

    /** 单个日志文件最大值MB */
    public static int MAXSIZE = 16;

    /**
     * 路径
     */
    public static class PATH{
        /**
         * SD根路径
         */
        public static String SDPath = Environment.getExternalStorageDirectory().getPath();

        /**
         * /data/data/包名/files
         * @param context
         * @return
         */
        public static String getAppFilesPath(Context context){
            return context.getFilesDir().getPath();
        }

        /**
         *  /data/data/包名/cache
         * @param context
         * @return
         */
        public static String getAppCachePath(Context context){
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 获取默认日志文件
     * /data/data/包名/cache/AppInfo/Bxlt/Log.log
     * @param context
     * @return
     */
    public static File getInstance(Context context){
        String path = PATH.getAppCachePath(context);
        File logFile = new File(path + "/AppInfo/Bxlt/Log" + ".log");
        File pathFile = logFile.getParentFile();
        if( !pathFile.exists()){
            pathFile.mkdirs();
        }
        if( !logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                System.out.println("com.bolt.log:创建默认日志文件失败");
                e.printStackTrace();
                return null;
            }
        }

        isSave = true;

        return logFile;
    }


    /**
     * 信息
     * @param args 变量入参
     */
    public static void i(String TAG, Object... args){
        if (args == null) {
            return;
        }

        if (!isInfo || !isLog){
            return;
        }

        String msgStr = String.format("%s%s:", INFO, TAG);

        makeLog(msgStr, args);
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
     * @param args
     */
    public static void d(String TAG, Object... args)
    {
        if (args == null) {
            return;
        }

        if( !isDebug || !isLog){
            return;
        }

        String msgStr = String.format("%s%s:", DENUG, TAG);

        makeLog(msgStr, args);

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
     * @param args
     */
    public static void e(String TAG, Object... args)
    {
        if(args == null ) {
            return;
        }
        if( !isError || !isLog){
            return;
        }

        String msgStr = String.format("%s%s:", ERROR, TAG);

        makeLog(msgStr, args);
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
     * @param args
     */
    public static void w(String TAG, Object... args)
    {
        if(args == null ) {
            return;
        }
        if(isWarning && isLog){
           return;
        }

        String msgStr = String.format("%s%s:", WARNING,TAG);

        makeLog(msgStr, args);

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
     * 生成日志
     * @param TAG
     * @param args
     */
    private static void makeLog(String TAG, Object... args){
        String msgStr = TAG;
        // 遍历入参
        for(int i = 0; i < args.length; i++){
            // 普通字符串
            if(args[i] instanceof String){
                msgStr += (String)args[i];
            }
            // 抛出的异常
            else if(args[i] instanceof Exception){
                Exception exists = (Exception) args[i];
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exists.printStackTrace(pw);
                msgStr += sw.toString();
            }
            if( i + 1 < args.length){
                msgStr += "--";
            }
        }

        System.out.println(msgStr); // 打印
        writeLog(msgStr);           // 写入文件
        appendOnView(msgStr);           // 显示到文本框
    }

    /**
     * 获取log文件
     * @return
     */
    public static String getLog(){
        return ReadTxtFile(logFile);
    }

    /**
     * 保存日志到SD卡
     * */
    private static void writeLog(String msg){

        if (!isSave){
            return;
        }
        if(logFile == null){
            try {
                throw new Exception(TAG + ":日志文件不存在，请检查路径");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        try {
            writeBySd(logFile,"<" +my_time_1 + "-" +  my_time_2 + ">--" +  msg + "\n",true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 清除日志文件, 删除后重新创建
     * */
    public static void clearLog(){
        try {
            if(logFile.exists()){
                logFile.delete();
                logFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加日志到UI
     * 只支持TextView和EditText显示，
     * 需要LOG.logView的可视性为VISIBLE。
     * 建议在视图不可见后，对视图进行clear
     * @param msg
     */
    private static void appendOnView(String msg){
        if(logView == null){
            return;
        }

        if(msg == null){
            return;
        }

        // 添加换行符
        if(!msg.endsWith("\n")){
            msg += "\n";
        }

        if(logView instanceof EditText){
            if(logView.getVisibility() == View.VISIBLE){
                ((EditText)logView).append(msg);
            }
        }
        else if(logView instanceof TextView){
            if(logView.getVisibility() == View.VISIBLE) {
                ((TextView) logView).append(msg);
            }
        }
    }

    /**
     * 文件写入到 Sd 卡根目录
     * @param logFile       日志文件
     * @param filecontent   日志内容
     * @param isAdd 是否追加
     * @return
     */
    public  static boolean writeBySd(File logFile, String filecontent,boolean isAdd) throws Exception {
        if(logFile == null) {
            throw new Exception(TAG + ":日志文件不存在，请检查路径");
        }
        else if(!logFile.exists()){
            logFile.createNewFile();
        }

        // 日志超大小，就复制备份
        if(logFile.length() / 1024 / 1024 > MAXSIZE){
            File newFile = new File(logFile.getParent() + "/LOG" +  Calendar.getInstance().getTime().getTime() + ".logback") ;
            copyFile(logFile, newFile);
            clearLog();
        }

        try {
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(logFile,isAdd);
            output.write(filecontent.getBytes());
            //将String字符串以字节流的形式写入到输出流中
            output.close();
            //关闭输出流
            return true;
        }catch (Exception e){
            d(TAG, "写入日志失败:路径为空");
            return false;
        }
    }

    /**
     * 读文件
     * @param logFile
     * @return
     */
    public static String ReadTxtFile(File logFile) {
        String content = ""; //文件内容字符串
        //打开文
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (logFile.isDirectory())
        {
            d(TAG, "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(logFile);
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
        String log = ReadTxtFile(logFile);

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

    /**
     * 复制文件
     * Copy file boolean.
     *
     * @param sourceFile the source file
     * @param targetFile the target file
     * @return the boolean
     */
    private static boolean copyFile(@NonNull File sourceFile, @NonNull File targetFile) {
        if (!sourceFile.exists() || targetFile.exists()) {
            //原始文件不存在，目标文件已经存在
            return false;
        }
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(targetFile);
            int temp;
            while ((temp = input.read()) != (-1)) {
                output.write(temp);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
        } finally {
            try {
                if( input != null) {
                    input.close();
                }
                if( output != null) {
                    output.close();
                }
            }catch (Exception e){

            }

        }
        return true;
    }


    public static File getLogFile() {
        return logFile;
    }

    public static void setLogFile(File logFile) {
        LOG.logFile = logFile;
    }

    public static View getLogView() {
        return logView;
    }

    public static void setLogView(View logView) {
        LOG.logView = logView;
    }
}
