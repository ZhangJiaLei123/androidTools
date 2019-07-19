package com.blxt.mbaseactivity;

import android.app.Activity;

import java.util.Stack;

/**
 * ActivityManager
 * @author: Zhang
 * @date: 2019/7/19 - 11:34
 * @note Created by com.blxt.mbaseactivity.
 */
public class ActivityManager {

    private static ActivityManager instance;
    private Stack<Activity> activityStack;// activity栈

    /**
     * 单例模式
     * @return
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }


    /**
     * 把一个activity压入栈中
     * @param actvity
     */
    public void addActivity(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(actvity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     * @return
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 移除一个activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }

        }
    }

    /**
     * 退出所有activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) {
                    break;
                }
                removeActivity(activity);
            }
        }

    }

}
