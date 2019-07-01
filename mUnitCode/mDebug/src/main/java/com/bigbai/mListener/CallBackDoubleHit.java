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
     */
    void onDoubleClick(View view, int touchTimes);

    /**
     * 完成连击
     * @param view
     * @param touchTimes
     */
    void onCompleteClick(View view, int touchTimes);
}
