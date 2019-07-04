package com.blxt.mbaseactivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.bigbai.mlog.LOG;

import java.util.Date;

/**
 * 屏保样板
 * @author: Zhang
 * @date: 2019/7/4 - 10:20
 * @note Created by com.blxt.mbaseactivity.
 */
public abstract class BaseScreenActivity extends BaseActivity{

    /** MSG id 关闭屏保程序 */
    public static final short MSGID_STOP_SCREENSAVER = 0;
    /** MSG id 开启屏保程序 */
    public static final short MSGID_OPEN_SCREENSAVER = 1;

    /** 标识当前是否进入了屏保&屏保是否运行*/
    public boolean isScreenSaverRun = false;

    /** 计时Handler */
    protected Handler mMakeTimeHandler = new Handler();
    /** 持续屏保Handler */
    protected Handler mKeepTimeHandler = new Handler();
    /** 上一次User有动作的Time Stamp */
    public Date lastUpdateTime;
    /** 计算User有几秒没有动作的 */
    protected long timePeriod;

    /** 静止超过N秒将自动进入屏保 */
    protected float mHoldStillTime = 10 * 60;
    /** 是否开启屏保 */
    protected boolean isScreensaverOpen = false;

    /** 时间间隔*/
    protected long intervalScreenSaver = 1000;
    protected long intervalKeypadeSaver = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 初始取得User可触碰屏幕的时间,放在onCreate中 */
        lastUpdateTime = new Date(System.currentTimeMillis());
    }

    @Override
    protected void onPause() { //Activity被覆盖到下面或者锁屏时被调用
        super.onPause();
        /** activity不可见的时候取消线程*/
        mMakeTimeHandler.removeCallbacks(mMakeTimeTask);
        mKeepTimeHandler.removeCallbacks(mKeepTask);
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据
    }
    @Override
    protected void onResume() {
        super.onResume();
        /** activity显示的时候启动线程*/
        mMakeTimeHandler.postAtTime(mMakeTimeTask, intervalKeypadeSaver);
    }


    /**
     * 更新屏幕操作时间
     * 用户有操作的时候不断重置静止时间和上次操作的时间
     * */
    public void updateUserActionTime() {
        Date timeNow = new Date(System.currentTimeMillis());
        timePeriod = timeNow.getTime() - lastUpdateTime.getTime();
        lastUpdateTime.setTime(timeNow.getTime());
    }

    /** 计时线程 */
    private Runnable mMakeTimeTask = new Runnable() {
        @Override
        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            /* 计算User静止不动作的时间间距 */
            /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
            timePeriod = (long) timeNow.getTime() - (long) lastUpdateTime.getTime();

            /*将静止时间毫秒换算成秒*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if(isScreensaverOpen && timePeriodSecond > mHoldStillTime){
                if(isScreenSaverRun == false){  //说明没有进入屏保
                    /* 启动线程去显示屏保 */
                    mKeepTimeHandler.postAtTime(mKeepTask, intervalScreenSaver);
                    /*显示屏保置为true*/
                    LOG.d("屏保", "启动线程去显示屏保>");
                    // 运行屏保，并标记
                    isScreenSaverRun = starScreenSaver();

                }else{
                    /*屏保正在显示中*/
                    LOG.d("屏保", "屏保正在显示中>");
                }
            }else{
                /*说明静止之间没有超过规定时长*/
                isScreenSaverRun = false;
            }
            /*反复调用自己进行检查*/
            mMakeTimeHandler.postDelayed(mMakeTimeTask, intervalKeypadeSaver);
        }
    };

    /** 持续屏保显示线程 */
    private Runnable mKeepTask = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isScreenSaverRun == true) {  //如果屏保正在显示，就计算不断持续显示
                mKeepTimeHandler.postDelayed(mKeepTask, intervalScreenSaver);
            } else {
                mKeepTimeHandler.removeCallbacks(mKeepTask);  //如果屏保没有显示则移除线程
            }
        }
    };

    @Override
    public boolean doMessage(Message msg){
        switch (msg.what)
        {
            case MSGID_OPEN_SCREENSAVER:
                isScreenSaverRun = starScreenSaver();
                return true;
            case MSGID_STOP_SCREENSAVER:
                isScreenSaverRun = stopScreenSaver();
                return true;

            default:break;
        }

        return false;
    }


    /**
     * 屏幕点击监听，更新用户操作时间
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        updateUserActionTime();
        return super.dispatchTouchEvent(ev);
    }

    /** 显示屏保,返回屏保是否运行成功 */
    public abstract boolean starScreenSaver();


    /** 停止屏保,返回屏保运行状态 */
    public abstract boolean stopScreenSaver();

    /** 获取屏保时间 */
    public float getmHoldStillTime() {
        return mHoldStillTime;
    }

    /** 设置屏保时间 */
    public void setmHoldStillTime(float mHoldStillTime) {
        this.mHoldStillTime = mHoldStillTime;
    }

    /** 开启屏保 */
    public void openScreensaver(){
        isScreensaverOpen = true;
    }
    /** 关闭屏保 */
    public void colseScreensaver(){
        isScreensaverOpen = false;
    }
    /** 获取屏保是否开启 */
    public boolean isScreensaverOpen(){
        return isScreensaverOpen;
    }

    /** 获取屏保程序是否正在运行 */
    public boolean isScreenSaverRun() {
        return isScreenSaverRun;
    }


}
