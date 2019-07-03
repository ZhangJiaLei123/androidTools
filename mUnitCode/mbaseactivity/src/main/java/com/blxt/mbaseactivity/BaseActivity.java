package com.blxt.mbaseactivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    protected static String TAG;
    protected static ViewHolder viewHolder; // ui管理
    Application application;                // Application单例A

    @SuppressLint("HandlerLeak")
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        context = this;
        activity = this;
        findViewById();
        initBaseUI();
        addOnClickListener();
    }


    /** 更新Data线程 */
    public void starRunData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                starRunUI();
            }
        }).start();
    }

    /** 更新UI线程 */
    public void starRunUI()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initUI();
            }
        });
    }

    //Activity创建或者从后台重新回到前台时被调用
    @Override
    protected void onStart() {
        super.onStart();
        application = getApplication();
        starRunData();
    }

    //Activity从后台重新回到前台时被调用
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //Activity创建或者从被覆盖、后台重新回到前台时被调用
    @Override
    protected void onResume() {
        super.onResume();
    }

    //Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    //Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
        super.onPause();
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据
    }

    //退出当前Activity或者跳转到新Activity时被调用
    @Override
    protected void onStop() {
        super.onStop();
    }

    //退出当前Activity时被调用,调用之后Activity就结束了
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 释放资源
     */
    @Override
    public void finish(){
        super.finish();
        release();
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putInt("ActivityParam", int ActivityParam);
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // int ActivityParam = savedInstanceState.getInt("ActivityParam");
        super.onRestoreInstanceState(savedInstanceState);
    }

    public Context getContext() {
        return this.context;
    }

    public Activity getActivity() {
        return activity;
    }

    /**
     * handler消息处理
     * @param msg
     */
    public abstract void doMessage(Message msg);

    /** findViewById，在 onCreate中自动调用 */
    public abstract void findViewById();

    /** 添加监听 ，在 onCreate中自动调用 */
    public abstract void addOnClickListener();

    /** 加载初始UI资源 */
    public abstract void initBaseUI();

    /** 在线程中获取加载UI,在initData后自动加载 */
    public abstract void initUI();

    /** 在线程中加载数据，在onStart中自动调用 */
    public abstract void initData();

    /** 资源释放, 在finish中自动调用 */
    public abstract void release();


}

