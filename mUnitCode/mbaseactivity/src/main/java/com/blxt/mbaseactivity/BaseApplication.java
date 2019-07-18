package com.blxt.mbaseactivity;

import android.Manifest;
import android.app.Application;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bigbai.mlog.LOG;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单例Application
 * 在Activity中获取单例的方法 ： (BaseApplication) getApplication()
 * @author: Zhang
 * @date: 2019/7/3 - 10:53
 * @note Created by com.blxt.mbaseactivity.
 */
public class BaseApplication extends Application {
    /**
     * 单例对象
     */
    public static BaseApplication instances;
    SysInfo sysInfo;
    /**
     * MainActivity的Handler
     */
    private Handler mMainACtivityHandler = null;

    /** SP信息 */
    public SharedPreferences g_appInfo;


    /**
     * 单例模式 * *
     */
    public static BaseApplication getInstances() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        sysInfo = new SysInfo();

        initSharedPreferences();

    }

    /**
     * 初始化 SharedPreferences
     */
    public void initSharedPreferences(){
        int _icountRunTimces = 0;
        // 获取Ips
        g_appInfo = getSharedPreferences(getPackageName() + "_default", 0);

        // 累加运行次数
        _icountRunTimces = g_appInfo.getInt("AppInfo_运行次数_",-1);
        // 然后写入
        g_appInfo.edit().putInt("AppInfo_运行次数_", _icountRunTimces + 1).commit();
        g_appInfo.edit().putString("AppInfo_上次运行时间_", Calendar.getInstance().getTime().getTime() + "").commit();
        g_appInfo.edit().putString("AppInfo_设备码", sysInfo.getOnlyId() ).commit();

    }

    /**
     * 获取主handdler
     * @return
     */
    public Handler getmMainACtivityHandler() {
        return mMainACtivityHandler;
    }

    /**
     * 添加主handdler
     * @return
     */
    public void setmMainACtivityHandler(Handler mMainACtivityHandler) {
        this.mMainACtivityHandler = mMainACtivityHandler;
    }

    /***********************************************************************************************
     *
     * 【线程池】
     *
     **********************************************************************************************/
    /** 可以实现循环或延迟任务的线程池 */
    public ScheduledExecutorService scheduledExecutorService = null;
    /** 回收型线程池 */
    public ExecutorService cachedThreadPool = null;
    /** 可控最大并发数线程池 */
    public ExecutorService fixedThreadPool = null;
    /** 单线程池 */
    public ExecutorService singleThreadExecutor = null;

    /**
     * 初始化单线程池
     */
    public void initSingleThreadExecutor(){

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(getPackageName() + "-Single-pool-%d").build();// 线程名

        singleThreadExecutor = Executors.newSingleThreadExecutor(namedThreadFactory);
    }

    /***
     * 初始化可以实现循环或延迟任务的线程
     * 默认执行顺序为FIFO
     * @param corePoolSize  核心线程池大小
     */
    public void initScheduledExecutorService(int corePoolSize,int maximumPoolSize, int keepAliveTime){
        scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern(getPackageName() + "-schedule-pool-%d").daemon(true).build());
    }

    /**
     * 初始化回收型线程池
     * @param corePoolSize              核心线程池大小
     * @param maximumPoolSize           线程池维护线程的最大数量
     * @param keepAliveTime             线程池维护线程所允许的空闲时间(默认时间单位是秒)
     */
    public void initcachedThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(getPackageName() + "-cached-pool-%d").build();// 线程名
        cachedThreadPool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                namedThreadFactory);
    }

    /**
     * 初始化可控最大并发数线程池
     * @param corePoolSize              核心线程池大小
     * @param maximumPoolSize           线程池维护线程的最大数量
     * @param keepAliveTime             线程池维护线程所允许的空闲时间(默认时间单位是秒)
     */
    public void initFixedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(getPackageName() + "-Fixed-pool-%d").build(); // 线程名

        fixedThreadPool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                namedThreadFactory);


    }


    /** 资源释放, 在finish中自动调用 */
    public void release(){
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdownNow();
        }
        if(cachedThreadPool != null){
            cachedThreadPool.shutdownNow();
        }
        if(fixedThreadPool != null){
            fixedThreadPool.shutdownNow();
        }
        if(singleThreadExecutor != null){
            singleThreadExecutor.shutdownNow();
        }

        g_appInfo.edit().putString("AppInfo_上次结束时间_", Calendar.getInstance().getTime().getTime() + "").commit();
    }


    public class SysInfo{

        /**
         * 获取唯一ID
         * @return
         */
        public String getOnlyId(){

            String m_szLongID = "";

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                m_szLongID = sysInfo.getPhoneIMEI() + sysInfo.getDevID()
                        + sysInfo.getAndroidID() + sysInfo.getBluetoothMAC();
            } else{
                m_szLongID =  sysInfo.getDevID()  + sysInfo.getAndroidID()
                        + sysInfo.getBluetoothMAC();
            }


            LOG.i("设备码5-" + m_szLongID);


            MessageDigest m = null;
            try {
                m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            m.update(m_szLongID.getBytes(),0,m_szLongID.length());
            byte p_md5Data[] = m.digest();
            String m_szUniqueID = new String();
            for (int i=0;i<p_md5Data.length;i++) {
                int b = (0xFF & p_md5Data[i]);
                if (b <= 0xF) {
                    m_szUniqueID += "0";
                }
                m_szUniqueID += Integer.toHexString(b);
                m_szUniqueID = m_szUniqueID.toUpperCase();
            }

            return m_szUniqueID;
        }

        private String getPhoneIMEI() {
            TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission( instances, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "null";
            }
            return tm.getDeviceId();
        }

        public String getBluetoothMAC(){
            BluetoothAdapter m_BluetoothAdapter = null;
            m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return m_BluetoothAdapter.getAddress();
        }

        public String getAndroidID(){
            return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        public String getDevID(){
            String m_szDevIDShort = "35" +
                    Build.BOARD.length()%10 +
                    Build.BRAND.length()%10 +
                    Build.CPU_ABI.length()%10 +
                    Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 +
                    Build.HOST.length()%10 +
                    Build.ID.length()%10 +
                    Build.MANUFACTURER.length()%10 +
                    Build.MODEL.length()%10 +
                    Build.PRODUCT.length()%10 +
                    Build.TAGS.length()%10 +
                    Build.TYPE.length()%10 +
                    Build.USER.length()%10 ;

            return m_szDevIDShort;
        }


    }

}
