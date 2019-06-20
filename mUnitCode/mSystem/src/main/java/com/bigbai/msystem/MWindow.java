package com.bigbai.msystem;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @brief：屏幕工具
 * 设置屏幕亮度
 * @author: Zhang
 * @date: 2019/6/15 - 16:12
 * @note Created by PACKAGE_NAME.
 */
public class MWindow {

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
     * 获得系统亮度
     * @param context
     * @return
    */
     public static int getBrightness(Context context) {
        int systemBrightness = 0;
        try {
                systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
           } catch (Settings.SettingNotFoundException e) {
               e.printStackTrace();
            }
        return systemBrightness;
     }

    /**
     * 设置当前屏幕的亮度
     * @param activity
     * @param brightness
     */
    public static void setBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }
}
