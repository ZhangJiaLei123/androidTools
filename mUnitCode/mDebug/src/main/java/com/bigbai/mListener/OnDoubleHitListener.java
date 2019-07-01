package com.bigbai.mListener;

import android.view.View;

import com.bigbai.mlog.LOG;

import java.util.Calendar;
import java.util.Date;


/**
 * 连击监听
 * @author: Zhang
 * @date: 2019/7/1 - 9:34
 * @note Created by pr.car.main.DEBUG.
 */
public class OnDoubleHitListener implements View.OnClickListener {

    /** 连击次数 */
    private short touchTimes = 0;
    /** 连击阈值*/
    private short touchTop = 5;
    /** 重置时间阈值 */
    private int resetTime = 1000;

    private Date lastTime = null;

    // 连击监听回调
    CallBackDoubleHit callBackDoubleHit = null;

    public OnDoubleHitListener(){

    }
    public OnDoubleHitListener(CallBackDoubleHit callBackDoubleHit) {
        this.callBackDoubleHit = callBackDoubleHit;
    }

    @Override
    public void onClick(View v) {

        Date timeDateNow = new Date(System.currentTimeMillis());
        if(lastTime == null){
            lastTime = timeDateNow;
        }

        /* 计算User静止不动作的时间间距 */
        /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
        long timePeriod = (long) timeDateNow.getTime() - (long) lastTime.getTime();

        // 连击时间小于阈值，就累加连击次数
        if( timePeriod <= resetTime){
            touchTimes++;

        }
        // 否者就清空连击次数
        else{
            touchTimes = 0;
        }

        if( this.callBackDoubleHit != null){
            this.callBackDoubleHit.onDoubleClick(v, touchTimes);
        }
        if( touchTimes >= touchTop){
            if( this.callBackDoubleHit != null){
                this.callBackDoubleHit.onCompleteClick(v, touchTimes);
            }
            touchTimes = 0;
        }

        lastTime = timeDateNow;
    }

    /**
     * 获取时间
     * @return
     */
    public static long getTime(){
        Date timeNow = new Date(System.currentTimeMillis());
        return timeNow.getTime();
    }

    public short getTouchTop() {
        return touchTop;
    }

    public void setTouchTop(short touchTop) {
        this.touchTop = touchTop;
    }

    public int getResetTime() {
        return resetTime;
    }

    public void setResetTime(int resetTime) {
        this.resetTime = resetTime;
    }

    public CallBackDoubleHit getCallBackDoubleHit() {
        return callBackDoubleHit;
    }

    public void setCallBackDoubleHit(CallBackDoubleHit callBackDoubleHit) {
        this.callBackDoubleHit = callBackDoubleHit;
    }
}
