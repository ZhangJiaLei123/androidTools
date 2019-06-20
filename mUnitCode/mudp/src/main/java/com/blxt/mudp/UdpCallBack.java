package com.blxt.mudp;

/**
 * @brief：UDP 接口回调
 * @author: Zhang
 * @date: 2019/6/19 - 11:29
 * @note Created by com.jietong.sanbu01.UDP.
 */
public interface UdpCallBack {
    public int sendData(byte[] data);
    public int receiveData(byte[] data);
}
