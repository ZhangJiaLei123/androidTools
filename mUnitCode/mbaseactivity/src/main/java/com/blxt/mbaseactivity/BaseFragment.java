package com.blxt.mbaseactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/***
 * base Fragment
 * @author Zhang
 */
public abstract class BaseFragment extends Fragment  {
    public String fragmentName = "Fragment"; // Fragment 名称
    public View rootView; // 根视图
    /** * 父Handler */
    private Handler activityHandler;
    @SuppressLint("HandlerLeak")
    public Handler mhandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };

    public BaseFragment(String fragmentName, Handler activityHandler){
        this.fragmentName = fragmentName;
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initUI();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        starRunData();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }

    /**
     * 需要重写hasMenu() 返回True，才会创建菜单
     *
     * @param menu     the menu
     * @param inflater the inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean hasMenu() {
        return false;
    }

    /**
     * 需要重写hasMenu() 返回True，才会回调
     * On options item selected boolean.
     *
     * @param item the item
     * @return the boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    /** findViewById，在 onCreate中自动调用 */
    public abstract void findViewById();

    /** 添加监听 ，在 onCreate中自动调用 */
    public abstract void addOnClickListener();

    /** 加载初始UI资源 */
    public abstract void initBaseUI();

    /** 在线程中获取加载UI,在initData后自动加载 */
    public abstract void initUI();

    /** 在线程中加载数据，在onCreate中自动调用 */
    public abstract void initData();


    /** 资源释放, 在finish中自动调用 */
    public abstract void release();

    /**
     * 返回键，预留给所在activity调用
     * On back pressed boolean.
     *
     * @return the boolean 如果本Fargment使用了BackPressed，就返回true
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * handler消息处理
     * @param msg
     */
   public abstract void doMessage(Message msg);

}
