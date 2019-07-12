package com.bigbai.mnetwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.bigbai.mnetwork.Ping.PingNet;
import com.bigbai.mnetwork.Ping.PingNetEntity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/***
 *  检查网络状态工具
 * @author Zhang
 * */
public class NetTools {

	/**
	 * ping工具
	 * @param hostIp	IP
	 * @param outTime	超时，单位为s
	 * @return
	 */
	public static int ping(String hostIp, int outTime) {
		PingNetEntity pingNetEntity=new PingNetEntity(hostIp,3, outTime,new StringBuffer());
		pingNetEntity= PingNet.ping(pingNetEntity);
		if (pingNetEntity.isResult()){
			return Integer.parseInt(pingNetEntity.getPingTime(), 10);
		}
		else{
			return -1;
		}
	}

	/**
	 * ping工具,3s超时
	 * @param hostIp ping Ip
	 * @return
	 */
	public static boolean ping(@NonNull String hostIp)
	{
		try
		{
			return InetAddress.getByName(hostIp).isReachable(3000);
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * 判断ip端口是否连接
	 * @param hostIp   主机
	 * @param port	   端口
	 * @return
	 */
	public static boolean checkHost(String hostIp, int port) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(hostIp, port));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 获取当前网络信息
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			return connectivity.getActiveNetworkInfo();
		}
		return null;
	}

	/**
	 * 判断以太网网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isEthernet(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mInternetNetWorkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
			boolean hasInternet = (mInternetNetWorkInfo != null) && mInternetNetWorkInfo.isConnected() && mInternetNetWorkInfo.isAvailable();
		 	return hasInternet;
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

	/**
	 * 判断Wifi是否打开
	 * @param context
	 * @return
	 */
	public static boolean isWifi(@NonNull Context context){
		ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return isWifi(mConnectivity);
	}

	/**
	 * 判断Wifi是否打开
	 * @param connMgr
	 * @return
	 */
	public static boolean isWifi(@NonNull ConnectivityManager connMgr){
		//检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
			return connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		}
		else {
			//获取所有网络连接的信息
			Network[] networks = connMgr.getAllNetworks();
			//通过循环将网络信息逐个取出来
			for (int i=0; i < networks.length; i++){
				//获取ConnectivityManager对象对应的NetworkInfo对象
				NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
				if("WIFI".equals(networkInfo.getTypeName()) && networkInfo.isConnected()) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * 判断数据网络是否打开
	 * @param context
	 * @return
	 */
	public static boolean isGprs(@NonNull Context context){
		ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return isGprs(mConnectivity);
	}

	/**
	 * 判断数据网络是否打开
	 * @param connMgr
	 * @return
	 */
	public static boolean isGprs(@NonNull ConnectivityManager connMgr){
		//获取移动数据连接的信息
		//检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
			NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(dataNetworkInfo == null){
				return false;
			}
			return dataNetworkInfo.isConnected();
		}
		else{
			//获取所有网络连接的信息
			Network[] networks = connMgr.getAllNetworks();
			//通过循环将网络信息逐个取出来
			for (int i=0; i < networks.length; i++){
				//获取ConnectivityManager对象对应的NetworkInfo对象
				NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
				if("MOBILE".equals(networkInfo.getTypeName()) && networkInfo.isConnected()) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * 判断是否有网络连接,如果没有联网就弹出Dialog并提示
	 * @param context
	 */
	public static boolean CheckNetworkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			return true;
		}

		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return true;
		}

		return false;

	}

    /**
     * 没有网络提示,如果没有联网就弹出Dialog并提示
     * @param context
     */
    private static void showTips(final Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            //	.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("网络错误")//设置对话框的标题
                .setMessage("当前网络不可用,是否设置网络？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 如果没有网络连接，则进入网络设置界面
                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
	}

}
