package com.bigbai.mListener;

import android.view.View;

/**
 * 连击监听回调
 * @author: Zhang
 * @date: 2019/7/1 - 9:47
 * @note Created by pr.car.main.DEBUG.
 */
public interface CallBackDoubleHit {

    /**
     * 连击回调
     * @param view          视图
     * @param touchTimes    连击次数
     * @return  返回连击次数，返回的连击次数将被应用到监听器
     */
    int onDoubleClick(View view, int touchTimes);

    /**
     * 完成连击
     * @param view          视图
     * @param touchTimes    连击次数
     * @return  是否所定连击，true时，连击被锁定，下次连击时不会相响应
     * 不需要时就返回false
     */
    boolean onCompleteClick(View view, int touchTimes);
}
