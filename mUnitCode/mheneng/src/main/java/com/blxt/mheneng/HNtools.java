package com.blxt.mheneng;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * @brief：对文件的简述
 * @author: Zhang
 * @date: 2019/7/13 - 22:07
 * @note Created by com.blxt.mheneng.
 */
public class HNtools {

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

    /**
     * 隐藏虚拟按键，并且全屏,全系统适用
     */
    public static boolean hideNavigation(){
        boolean ishide;
        try
        {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            Process proc = Runtime.getRuntime().exec(new String[] { "su","-c", command });
            proc.waitFor();
            ishide = true;
        }
        catch(Exception ex)
        {
            ishide = false;
        }
        return ishide;
    }

    /**
     * 显示虚拟按键
     * */
    public static boolean showNavigation(){
        boolean isshow;
        try
        {
            String command;
            command  =  "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            Process proc = Runtime.getRuntime().exec(new String[] { "su","-c", command });
            proc.waitFor();
            isshow = true;
        }
        catch (Exception e)
        {
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }

}
