package com.bigbai.anunit.NoticeUnit;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TipDialog extends Dialog{
	/**
		 * 
		 * @param context 上下文
		 * @param theme 设置该View的样式
		 */
		protected TipDialog(Context context, int theme) {
			super(context, theme);
			// TODO Auto-generated constructor stub
		}
		/**
		 * 
		 * @param context 上下文
		 * @param theme 设置该View的样式
		 * @param view  设置该View
		 * @param state 设置该布局的位置在 （1111：最上边， 2222：最下边， 3333：最中间）
		 */
		public TipDialog(Context context, int theme, View view, String state) {
			super(context, theme);
			setContentView(view);
			Window wi = getWindow();
			
			WindowManager.LayoutParams params = wi.getAttributes();
			if(state.equals("TOP"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
			}
			else if(state.equals("BOTTOM"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			}
			else if(state.equals("CENTER"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL;
			}
		}
		/**
		 * 
		 * @param context 上下文
		 * @param theme 设置该View的样式
		 * @param view  设置该View
		 * @param state 设置该布局的位置在 （1111：最上边， 2222：最下边， 3333：最中间）
		 */
		public TipDialog(Context context, int theme, int view, String state) {
			super(context, theme);
			setContentView(view);
			Window wi = getWindow();
			
			WindowManager.LayoutParams params = wi.getAttributes();
			if(state.equals("TOP"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
			}
			else if(state.equals("BOTTOM"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			}
			else if(state.equals("CENTER"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL;
			}
		}
		/**
		 * 
		 * @param context
		 * @param theme  设置该View的样式
		 * @param view   设置该View
		 * @param width	  设置该View显示的宽度
		 * @param height 设置该View显示的高度
		 */
		public TipDialog(Context context, int theme, View view, int width, int height, String state) {
			super(context, theme);
			setContentView(view);
			Window wi = getWindow();
	
			WindowManager.LayoutParams params = wi.getAttributes();
			params.width = (int)width;
			params.height = (int)height;
			if(state.equals("TOP"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
			}
			else if(state.equals("BOTTOM"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			}
			else if(state.equals("CENTER"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL;
			}
		}
		/**
		 * 
		 * @param context
		 * @param theme  设置该View的样式
		 * @param view   设置该View
		 * @param width	  设置该View显示的宽度
		 * @param height 设置该View显示的高度
		 */
		public TipDialog(Context context, int theme, int view, int width, int height, String state) {
			super(context, theme);
			
			setContentView(view);
			Window window = getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			params.width = (int) (width);  
	        params.height = (int) (height);  
	    	if(state.equals("TOP"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
			}
			else if(state.equals("BOTTOM"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			}
			else if(state.equals("CENTER"))
			{
				params.gravity = Gravity.CENTER_HORIZONTAL;
			}
	        this.setCanceledOnTouchOutside(false);
	        window.setAttributes(params);  
		}
	}
