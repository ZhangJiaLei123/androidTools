package com.bigbai.watchdog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 简单看门狗服务
 * @author: Zhang
 * @date: 2019/7/2 - 9:11
 * @note Created by com.bigbai.watchdog.
 */
public class WatchdogService extends Service {
    List<Wdog> wdogs = null;
    WatchdogCallBack watchdogCallBack = null;
    // 遛狗的定时器
    private ScheduledExecutorService mScheduledExecutorService = null;

    /** 任务启动延时时间 */
    long initialDelay = 5;
    /** 看门口遛狗周期，默认单位为秒 */
    long delay = 1;
    /** 看门狗遛狗单位，默认单位为秒 */
    TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 遛狗任务，遛完狗后检查看门狗的饥饿度，饿死了就触发犬吠
     */
    public Runnable runnableCheacLive = new Runnable() {
        @Override
        public void run() {
            if(wdogs == null || wdogs.size() <=0){
                return;
            }

            watchdogCallBack.walk(wdogs);
            for(int i = 0; i < wdogs.size(); i++){
                if(wdogs.get(i).sports() <= 0 && wdogs.get(i).isLive()){ // 饥饿值为0时就触发犬吠
                    watchdogCallBack.dogBark(wdogs.get(i));
                    wdogs.get(i).setDie();
                }
            }
        }
    };

    /** 核心线程大小 */
    static int corePoolSize = 1;

    public WatchdogService(){

    }

    public WatchdogService(WatchdogCallBack watchdogCallBack){
        this.watchdogCallBack = watchdogCallBack;

        mScheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize,
                new ThreadFactoryBuilder().setNameFormat("BLXT-Watchdog-task-%d").build());
        // 但周期是上一次任务结束和下一次任务开始的时间间隔

    }

    public void start(){
        mScheduledExecutorService.scheduleWithFixedDelay(runnableCheacLive,initialDelay, delay, unit);//延时5秒执行
    }

    public void stop(){
        mScheduledExecutorService.shutdownNow();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 给指定id的看门狗喂食
     * 饿混了的狗，无法喂食
     */
    public boolean feed(int id){
        if(wdogs == null || wdogs.size() <=0){
            return false;
        }

        for(int i = 0; i < wdogs.size(); i++){
            if(wdogs.get(i).getId() == id){ // 给指定id的看门狗喂食
                if(wdogs.get(i).feed() <= 0){ // 饿混了的狗，无法喂食
                  return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * 给指定id的看门狗喂食
     * 饿混了的狗，无法喂食
     */
    public boolean feed(Wdog wdog){
        if(wdogs == null || wdogs.size() <=0){
            return false;
        }

        for(int i = 0; i < wdogs.size(); i++){
            if(wdogs.get(i).getId() == wdog.getId()){ // 给指定id的看门狗喂食
                if(wdogs.get(i).feed() <= 0){ // 饿混了的狗，无法喂食
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /** 给全部看门狗喂食 */
    public boolean feed(){
        if(wdogs == null || wdogs.size() <=0){
            return false;
        }
        for(int i = 0; i < wdogs.size(); i++){

            if(wdogs.get(i).feed() <= 0){ // 喂食失败的
                watchdogCallBack.dogBark(wdogs.get(i));
            }
        }

        return true;
    }

    /***
     * 添加看门狗
     * @return
     */
    public boolean  addDog(Wdog wdog){
        if(wdogs == null || wdogs.size() <=0){
            wdogs = new ArrayList<>();
        }

        for(int i = 0; i < wdogs.size(); i++){
            if(wdogs.get(i).getId() == wdog.getId()){ // 不允许看门狗ID存在
                return false;
            }
        }

        this.wdogs.add(wdog);

        return true;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public List<Wdog> getWdogs() {
        return wdogs;
    }
}
