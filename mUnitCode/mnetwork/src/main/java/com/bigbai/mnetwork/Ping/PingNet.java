package com.bigbai.mnetwork.Ping;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 new Thread(new Runnable() {
@Override
public void run() {
         PingNetEntity pingNetEntity=new PingNetEntity(purecameraip,3,5,new StringBuffer());
         pingNetEntity=PingNet.ping(pingNetEntity);
         if (pingNetEntity.isResult()){
             Log.i("Ping测试",pingNetEntity.getIp());
             Log.i("Ping测试","time="+pingNetEntity.getPingTime());
             Log.i("Ping测试",pingNetEntity.isResult()+"");
         }
         else{
          //UserControl.ui.Error("IP不存在");
         }
    }
}).start();
 */
public class PingNet {
    private static final String TAG = "PingNet";
    private static boolean isLog = false;

    /**
     * @param pingNetEntity 检测网络实体类
     * @return 检测后的数据
     */
    public static PingNetEntity ping(PingNetEntity pingNetEntity) {
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
        String command = "ping -c " + pingNetEntity.getPingCount() + " -w " + pingNetEntity.getPingWtime() + " " + pingNetEntity.getIp();
//        String command = "ping -c " + pingCount + " " + host;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                if (isLog) {
                    Log.e(TAG, "ping fail:process is null.");
                }
                append(pingNetEntity.getResultBuffer(), "ping fail:process is null.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
                return pingNetEntity;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                if (isLog) Log.i(TAG, line);
                append(pingNetEntity.getResultBuffer(), line);
                String time;
                if ((time = getTime(line)) != null) {
                    pingNetEntity.setPingTime(time);
                }
            }
            int status = process.waitFor();
            if (status == 0) {
                if (isLog) Log.i(TAG, "exec cmd success:" + command);
                append(pingNetEntity.getResultBuffer(), "exec cmd success:" + command);
                pingNetEntity.setResult(true);
            } else {
                if (isLog) Log.e(TAG, "exec cmd fail.");
                append(pingNetEntity.getResultBuffer(), "exec cmd fail.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
            }
            if (isLog) Log.i(TAG, "exec finished.");
            append(pingNetEntity.getResultBuffer(), "exec finished.");
        } catch (IOException e) {
            if (isLog) Log.e(TAG, String.valueOf(e));
        } catch (InterruptedException e) {
            if (isLog) Log.e(TAG, String.valueOf(e));
        } finally {
            if (isLog) Log.i(TAG, "ping exit.");
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    Log.e(TAG, String.valueOf(e));
                }
            }
        }
        if (isLog) Log.i(TAG, pingNetEntity.getResultBuffer().toString());
        return pingNetEntity;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }

    private static String getTime(String line) {
        String[] lines = line.split("\n");
        String time = null;
        for (String l : lines) {
            if (!l.contains("time="))
                continue;
            int index = l.indexOf("time=");
            time = l.substring(index + "time=".length());
            if (isLog) Log.i(TAG, time);
        }
        return time;
    }
}

