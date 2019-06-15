package com.bigbai.msystem;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * @brief：屏幕工具
 * @author: Zhang
 * @date: 2019/6/15 - 16:12
 * @note Created by PACKAGE_NAME.
 */
public class MWindow {
    /**
     * 获得系统亮度
     * @param context
     * @return
    */
     private int getBrightness(Context context) {
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
    public void setBrightness(Activity activity, int brightness) {
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
