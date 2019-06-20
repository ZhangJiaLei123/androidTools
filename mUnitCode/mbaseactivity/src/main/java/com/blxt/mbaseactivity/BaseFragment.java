package com.blxt.mbaseactivity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;


/***
 * base Fragment
 * @author Zhang
 */
public abstract class BaseFragment extends Fragment {
    public String fragmentName = "Fragment"; // Fragment 名称
    View rootView; // 根视图
    public ViewHolder viewHolder; // 视图
    /** * 父Handler */
    public Handler handlerFather;
    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
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
    }


    public Handler getHandler(){
        return handler;
    }

    /**
     * handler消息处理
     * @param msg
     */
   public abstract void doMessage(Message msg);

}
