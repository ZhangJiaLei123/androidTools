package com.blxt.mbaseactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

/**
 * 欢迎界面模板
 */
public class WellComeActivity extends BaseActivity {

    Thread thread = null;
    Runnable runnable = null;

    boolean isSpik = false;

    /** layout 资源 ID*/
    public static int layoutId = 0;

    /** 开始欢迎界面 */
    public static void startWellCome(Context context, int ResourceId){
        WellComeActivity.layoutId = ResourceId;
        Intent intent = new Intent();
        intent.setClass(context, WellComeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(WellComeActivity.layoutId);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(isSpik) {
                    finish();
                }
            }
        };

        thread = new Thread(runnable);

        thread.start();
    }

    @Override
    public void doMessage(Message msg) {

    }

    @Override
    public void findViewById() {

    }

    @Override
    public void addOnClickListener() {

    }

    @Override
    public void initBaseUI() {

    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void release() {

    }

}
