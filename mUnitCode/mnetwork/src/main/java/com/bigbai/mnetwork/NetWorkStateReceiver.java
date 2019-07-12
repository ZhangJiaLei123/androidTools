package com.bigbai.mnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * 网络状态监听,注册时，会自动提示一次网络变更，
 * 网络变更后，会有5s左右延时响应
 * 记得unregisterReceiver注销。
 * @author: Zhang
 * @date: 2019/7/9 - 13:37
 * @note Created by com.blxt.test.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private CallBack callBack = null;
    /**
     * 动态注册广播
     * @param context
     * @return
     */
    public static NetWorkStateReceiver getInstance(Context context, @NonNull CallBack callBack) {
        NetWorkStateReceiver netWorkStateReceiver = new NetWorkStateReceiver();
        netWorkStateReceiver.setCallBack(callBack);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(netWorkStateReceiver, filter);

        return netWorkStateReceiver;
    }


    /**
     * 注销广播
     * @param context
     * @param broadcastReceiver
     */
    public static void unregisterReceiver(Context context,
                                          BroadcastReceiver broadcastReceiver){
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(callBack != null) {
            callBack.onNetWorkChange(connMgr);
        }
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 网络变化监听回调
     */
    public interface CallBack{
        /***
         * 网络变化监听回调
         * @return
         */
        boolean onNetWorkChange(ConnectivityManager connMgr);
    }
}


