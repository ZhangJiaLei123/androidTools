package com.bigbai.mnetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;


/***
 *  检查网络状态
 * 
 * */
public class CheckNet {
	
	
	 /**
	  * 判断网络是否是以太网
	  * @param mContext
	  * @return
	  */
    public boolean isEthernet(Context mContext) {

            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo NetInfo = connectivityManager
                            .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);//这里就是判断是否以太网
            boolean available = NetInfo.isAvailable();
            if (available)

            {

                    return true;
            }
            return false;
    }
	
	
	
	/**
	  * 检测网络是否连接(包括3G和Wifi网)
	  * @return
	  */
	public static boolean checkNet(Context context) {     

       try {     
           ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);     
           if (connectivity != null) {     

               NetworkInfo info = connectivity.getActiveNetworkInfo();
               if (info != null && info.isConnected()) {     

                   if (info.getState() == State.CONNECTED) {
                       return true;     
                   }     
               }     
           }     
       } catch (Exception e) {     
       return false;     
       
       
       }     
       return false;     
   }    
	     
	
	/**判断WIFI网络是否打开
	 * @param activity
	 * @return
	 */
		public static boolean isWifiActive(Activity activity){
			ConnectivityManager mConnectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(mConnectivity != null){
				NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();
				
				if(infos != null){
					for(NetworkInfo ni: infos){
						if("WIFI".equals(ni.getTypeName()) && ni.isConnected())
							return true;
					}
				}
			}
			return false;
		}
		/**
	 * 判断网络是否打开
	 * @param context
	 * @return
	 */
		public static boolean isWifiActive(Context context){
			ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(mConnectivity != null){
				NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();
				
				if(infos != null){
					for(NetworkInfo ni: infos){
						if("WIFI".equals(ni.getTypeName()) && ni.isConnected())
							return true;
					}
				}
			}
			return false;
		}
	
		/**
		 * 判断是否有网络连接,如果没有联网就弹出Dialog并提示
		 * @param activity
		 */
		public static void CheckNetworkState(Activity activity) {
			ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
			if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
				return;
			}

			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				return;
			}
			showTips(activity);
		}

    /**
     * 没有网络提示
     * @param activity
     */
    private static void showTips(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
            //	.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("网络错误")//设置对话框的标题
                .setMessage("当前网络不可用,是否设置网络？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                        activity.finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 如果没有网络连接，则进入网络设置界面
                        activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
	}
		
}
