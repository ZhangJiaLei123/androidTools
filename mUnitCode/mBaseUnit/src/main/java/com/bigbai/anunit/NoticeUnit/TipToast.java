package com.bigbai.anunit.NoticeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Toast封装类
 * @author peter
 *
 */
public class TipToast{
	/**
	 *
	 * @param resourceId 传入的布局
	 * @param context  上下文
	 * @param state  设置该布局的位置在 （1111：最上边， 2222：最下边， 3333：最中间）
	 * @param date  设置Toast显示时间长短（1101：长， 1102：短）
	 */
	public static void showToast(int resourceId, Context context, String state, String date) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View toastView = inflater.inflate(resourceId, null);
			toastView.setBackgroundColor(Color.TRANSPARENT);
	    Toast mToast = null;
	    if (mToast == null) {
	        mToast = new Toast(context);
	        mToast.setView(toastView);
	        if(state.equals("TOP"))
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.TOP, 0, 100);
	        }
	        else if(state.equals("BOTTOM"))
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.BOTTOM, 0, 100);
	        }
	        else if (state.equals("CENTER"))
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
	        }
	        if(date.equals("SHORT"))
	        {
	        	mToast.setDuration(Toast.LENGTH_SHORT);
	        }
	        else if(date.equals("LONG"))
	        {
	        	mToast.setDuration(Toast.LENGTH_LONG);
	        }
	    }
	    mToast.show();
	}

	/**
	 * @param context  上下文
	 * @param msg       消息
	 */
	public static void showToast(Context context, String msg[])
	{
		String info = "";
		for(String s : msg){
			info += s + "";
		}
		Toast.makeText(context,info, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String msg)
	{
		Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
	}


	//  TipToast.showToast(getApplicationContext(),handler,(String) msg.obj);

	/**
	 *
	 * @param context  getApplicationContext()
	 * @param handler  Handler handler
	 * @param msg      (String) msg.obj
	 */
	public static void showToast(final Context context,final Handler handler , final String msg){
		// 新启动一个子线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 使用post方式加到主线程的消息队列中
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context,
								msg,Toast.LENGTH_LONG).show();
					}
				});
			}
		}).start();

	}

}

