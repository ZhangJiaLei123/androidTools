package com.blxt.mudp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @brief：UDP客户端工具
 * @author: Zhang
 * @date: 2019/6/20 - 15:42
 * @note Created by com.blxt.mudp.
 */
public class MUdpClient {
    public static String TAG = "MUdpClient";
    /** 最大缓存字节 */
    private int MAX_BUFF_LENG = 1024;
    private String addr = "";

    int prot = 8080;
    public DatagramSocket socket = null;
    InetAddress serverAddress = null;

    DatagramPacket packetRec = null;
    UdpCallBack callBack = null;
    /** 是否接受消息 */
    boolean isGetReceive = true;

    /**
     * 创建客户端连接
     * @param addr      地址
     * @param prot      端口
     */
    public MUdpClient(String addr, int prot){
        this.addr = addr;
        this.prot = prot;
        try {
            socket = new DatagramSocket(this.prot);
            serverAddress = InetAddress.getByName(addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(socket == null){
            Log.e(TAG,"socket创建");
        }
    }


    /**
     * 发送字节
     * @param data
     */
    public void sendByte(byte data[]){
        try{
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, prot);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送字节
     * @param data
     */
    public void sendByteThread(final byte data[]){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendByte(data);
            }
        }).start();
    }

    /**
     *  接收指定长度的字节
     * @param leng
     * @param fal
     */
    public byte[] receiveMessage(int leng, boolean fal){
        byte[] data = null;
        if(socket == null){
            return data;
        }
        try {
            byte[] receBuf = new byte[MAX_BUFF_LENG];
            packetRec = new DatagramPacket(receBuf, receBuf.length);
            socket.receive(packetRec);
            if(callBack != null){ // 收到消息
                int len = 0;
                if(fal) { len = leng;
                }
                else{
                    len = packetRec.getLength();
                }
                data = new byte[len];
                // 截取有效长度
                System.arraycopy(packetRec.getData(), 0, data, 0, len);
                callBack.receiveData(data, len); // 发送给回调接口
            }
        } catch (IOException e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 接收指定长度的字节
     * @param leng
     */
    public void receiveMessageThread(final int leng, final boolean fal){
        new Thread() {
            @Override
            public void run() {
                while (isGetReceive) {
                    receiveMessage(leng, false);
                }
            }
        }.start();
    }

    public void receiveMessageThread(int leng) {
        receiveMessageThread(leng, true);
    }
    /**
     * 开始接受消息
     */
    public void receiveMessageThread() {
        receiveMessageThread(0, false);
    }


    /**
     * 停止接收
     */
    public void stopReceive(){
        isGetReceive = false;
    }


    /**
     * 关闭连接
     */
    public void close(){
        socket.close();
    }

    public UdpCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(UdpCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 设置最大缓存字节
     * @param MAX_BUFF_LENG
     */
    public void setMAX_BUFF_LENG(int MAX_BUFF_LENG){
        this.MAX_BUFF_LENG = MAX_BUFF_LENG;
    }
}
