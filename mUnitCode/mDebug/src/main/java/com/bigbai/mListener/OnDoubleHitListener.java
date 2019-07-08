package com.bigbai.mListener;

import android.view.View;

import java.util.Date;


/**
 * 连击监听
 * @author: Zhang
 * @date: 2019/7/1 - 9:34
 * @note Created by pr.car.main.DEBUG.
 */
public class OnDoubleHitListener implements View.OnClickListener {

    /** 连击次数 */
    private int touchTimes = 0;
    /** 连击阈值*/
    private short touchTop = 5;
    /** 重置时间阈值 */
    private int resetTime = 1000;
    /** 上次点击时间 */
    private Date lastTime = null;

    /** 上次点击的ViewID */
    private int uiViewIdLast = 0;

    /** 是否锁定同一View的连击，即连击的View不同时，释放连击,默认True */
    private boolean isLackView = true;

    /** 连击锁定,true时无法响应连击 */
    boolean isLack = false;

    // 连击监听回调
    CallBackDoubleClick callBack = null;


    public OnDoubleHitListener(CallBackDoubleClick callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onClick(View v) {
        // 判断是否锁定连击
        if(isLack){
            callback(v);
            return;
        }

        // 判断是否是相同View
        if(uiViewIdLast != 0 && isLackView){
            if(uiViewIdLast != v.getId()){
                callback(v);
                return;
            }
        }
        // 记录ViewId
        uiViewIdLast = v.getId();

        // 连击时间判定
        Date timeDateNow = new Date(System.currentTimeMillis());
        if(lastTime == null){
            lastTime = timeDateNow;
        }

        /* 计算User静止不动作的时间间距 */
        /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
        long timePeriod = (long) timeDateNow.getTime() - (long) lastTime.getTime();
        // 连击时间小于阈值，就累加连击次数
        if( timePeriod <= resetTime && timePeriod > 0){
            touchTimes++;
        }
        // 否者就清空连击次数
        else{
            touchTimes = 0;
        }

        // 点击回调
        callback(v);

        // 连击完成回调
        if( touchTimes >= touchTop){
            isLack = true; // 标记连击锁
            if( this.callBack != null){
                isLack = this.callBack.onCompleteClick(v, touchTimes);
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

    private void callback(View v){
        if( this.callBack != null){
            touchTimes = this.callBack.onClick(v, touchTimes);
        }
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

    public CallBackDoubleClick getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBackDoubleClick callBackDoubleHit) {
        this.callBack = callBackDoubleHit;
    }

    /**
     * 解除连击锁定
     */
    public void unlock(){
        isLack = false;
    }

    public boolean isLack(){
        return isLack;
    }

    /**
     * 连击结果回调
     * */
    public interface CallBackDoubleClick {

        /**
         * 连击回调
         * @param view          视图
         * @param touchTimes    连击次数
         * @return  返回连击次数，返回的连击次数将被应用到监听器
         */
        int onClick(View view, int touchTimes);

        /**
         * 完成连击
         * @param view          视图
         * @param touchTimes    连击次数
         * @return  是否所定连击，true时，连击被锁定，下次连击时不会相响应
         * 不需要时就返回false
         */
        boolean onCompleteClick(View view, int touchTimes);
    }


}
