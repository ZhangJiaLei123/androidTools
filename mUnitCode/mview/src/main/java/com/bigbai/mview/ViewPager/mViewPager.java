package com.bigbai.mview.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 简单的ViewPager,
 * 1. 可使能左右滑动
 */
public class mViewPager extends ViewPager {


    /** 滑动使能 true 允许滑动 false 禁止滑动*/
    private boolean isScrollEnable = true;

    public mViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mViewPager(Context context) {
        super(context);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!isScrollEnable) {
            return false;
        }
        else {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isScrollEnable) {
            return false;
        }
        else {
            return super.onInterceptTouchEvent(arg0);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    /**
     * 获取是否禁止滑动
     * @return
     */
    public boolean isScrollEnable() {
        return isScrollEnable;
    }

    /**
     * 是否屏蔽左右滑动
     * @param scrollEnable
     */
    public void setScrollEnable(boolean scrollEnable) {
        isScrollEnable = scrollEnable;
    }
}