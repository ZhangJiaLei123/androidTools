package com.blxt.mbaseactivity;

import android.app.Application;
import android.os.Handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

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
    /**
     * MainActivity的Handler
     */
    private Handler mMainACtivityHandler = null;

    private BaseAppConfig appConfig = null;

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

    }

    public Handler getmMainACtivityHandler() {
        return mMainACtivityHandler;
    }

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
}
