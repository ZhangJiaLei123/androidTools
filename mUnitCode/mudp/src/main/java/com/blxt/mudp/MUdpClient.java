package com.blxt.mudp;

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
    private String addr = "";

    int prot = 8080;
    DatagramSocket socket = null;
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
    }



    /**
     * 发送字节
     * @param data
     */
    public void sendByte(final byte data[]){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, prot);
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 开始接受消息
     */
    public void receiveMessage() {

        new Thread() {
            @Override
            public void run() {
                byte[] receBuf = new byte[1024];
                packetRec = new DatagramPacket(receBuf, receBuf.length);
                while (isGetReceive) {
                    try {
                        socket.receive(packetRec);
                        if(callBack != null){ // 收到消息
                            byte[] data = new byte[packetRec.getLength()];
                            // 截取有效长度
                            System.arraycopy(packetRec.getData(), 0, data, 0, packetRec.getLength());
                            callBack.receiveData(data); // 发送给回调接口
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
}
