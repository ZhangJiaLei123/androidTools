package com.bigbai.mnoticeunit;

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
	public static final int GRAVITY_TOP = 1;
	public static final int GRAVITY_BOTTOM = 2;
	public static final int GRAVITY_CENTER = 3;

	public static final int TIME_SHORT = Toast.LENGTH_SHORT;
	public static final int TIME_LONG = Toast.LENGTH_LONG;
	/**
	 *
	 * @param resourceId 传入的布局
	 * @param context  上下文
	 * @param state    设置该布局的位置在@see TipToast
	 * @param timeModel  显示时间长短。@see TipToast
	 */
	public static void showToast(int resourceId, Context context, int state, int timeModel) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View toastView = inflater.inflate(resourceId, null);
			toastView.setBackgroundColor(Color.TRANSPARENT);
	    Toast mToast = null;
	    if (mToast == null) {
	        mToast = new Toast(context);
	        mToast.setView(toastView);
	        if(state == TipToast.GRAVITY_TOP)
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.TOP, 0, 100);
	        }
	        else if(state == TipToast.GRAVITY_BOTTOM)
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.BOTTOM, 0, 100);
	        }
	        else if (state == TipToast.GRAVITY_CENTER)
	        {
	        	mToast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
	        }


	        mToast.setDuration(timeModel);

	    }
	    mToast.show();
	}

	/**
	 * @param context  上下文
	 * @param args     消息
	 */
	public static void showToast(Context context, Object... args)
	{
		String info = "";
		for(int i = 0; i < args.length; i++){
			info += args[i] + "";
		}
		Toast.makeText(context,info, Toast.LENGTH_SHORT).show();
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

