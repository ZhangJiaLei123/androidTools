package com.bigbai.msystem;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * @brief：对文件的简述
 * @author: Zhang
 * @date: 2019/6/16 - 21:28
 * @note Created by com.bigbai.msystem.
 */
public class MSystemTool {
    /**
     * 隐藏虚拟按键，并且全屏,全系统适用
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
