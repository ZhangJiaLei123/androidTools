package com.blxt.mbaseactivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/***
 * 通用ViewHolder模板
 */
public abstract class ViewHolder {
    Context context;
    Activity activity;
    View rootView;

//    public static ViewHolder getInstance(View rootView){
//        return new ViewHolder(rootView) ;
//    }

    public ViewHolder(View rootView) {
        this.rootView = rootView;
        init(rootView);
    }

    public ViewHolder(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        init(rootView);
    }

    public ViewHolder(Activity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
        init(rootView);
    }

    public ViewHolder(Context context, Activity activity, View rootView) {
        this.context = context;
        this.activity = activity;
        this.rootView = rootView;
        init(rootView);
    }

    /**
     * 统一的初始化数据
     * @param rootView
     */
    public void init(View rootView){
        initUI(rootView);
        initData();
        initUIData();
    }

    /**
     * 初始化UI
     *
     */
    public abstract void initUI(View rootView);

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化UI数据，并添加监听
     */
    public abstract void initUIData();


}
